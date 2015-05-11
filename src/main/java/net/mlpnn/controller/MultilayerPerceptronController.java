package net.mlpnn.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import net.mlpnn.constants.ResourcePath;
import net.mlpnn.dto.DashBoardDTO;
import net.mlpnn.dto.NetworkStatusDTO;
import net.mlpnn.dto.SigmaGraphDTO;
import net.mlpnn.enums.DataSetInfo;
import net.mlpnn.form.MultilayerPercetpronParametersForm;
import net.mlpnn.service.GraphService;
import net.mlpnn.service.I18NService;
import net.mlpnn.service.MultiLayerPerceptronRunner;
import net.mlpnn.service.MultiLayerPerceptronService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Lindes Roets
 */
@Controller
@RequestMapping(ResourcePath.MLP_BASE)
public class MultilayerPerceptronController extends BaseController {

	private final Logger LOGGER = LoggerFactory.getLogger(DatasetController.class);

	@Autowired
	MultiLayerPerceptronService multiLayerPerceptronService;

	@Autowired
	GraphService graphService;

	@Autowired
	I18NService i18n;

	@RequestMapping(value = "/train", method = RequestMethod.POST)
	public String train(Model model, @Valid @ModelAttribute(value = "mlpForm") MultilayerPercetpronParametersForm form, BindingResult result) {
		if (!result.hasErrors()) {
			multiLayerPerceptronService.startLearning(form);
			return "redirect:/mlp/dashboard";
		} else {
			return "mlp-create";
		}
	}

	@RequestMapping(value = "/train", method = RequestMethod.GET)
	@ResponseBody
	public String train(@ModelAttribute(value = "mlpForm") MultilayerPercetpronParametersForm multilayerPercetpronParametersForm) {
		String runnerID = multiLayerPerceptronService.startLearning(multilayerPercetpronParametersForm);
		return runnerID;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String viewAll(Model model) throws InterruptedException {
		List<NetworkStatusDTO> statuses = multiLayerPerceptronService.getPerceptronStatuses();
		model.addAttribute("statuses", statuses);
		return "mlp-list";
	}

	@RequestMapping(value = "/dashboard/info", method = RequestMethod.GET)
	@ResponseBody
	public DashBoardDTO dashboardInfo() throws InterruptedException {
		DashBoardDTO dto = multiLayerPerceptronService.getDashBoard();
		return dto;
	}

	@RequestMapping(value = "/dashboard")
	public String dashboard() {
		return "mlp-dashboard";
	}

	@RequestMapping(value = ResourcePath.MLP_CREATE, method = RequestMethod.GET)
	public String create(Model model) {
		model.addAttribute("mlpForm", new MultilayerPercetpronParametersForm());
		return "mlp-create";
	}

	@RequestMapping(value = "/{mlpId}/view", method = RequestMethod.GET)
	public String view(Model model, @PathVariable String mlpId, RedirectAttributes redirect) {
		MultiLayerPerceptronRunner runner = multiLayerPerceptronService.getMultiLayerPerceptronRunners().get(mlpId);
		if (runner == null) {
			redirect.addFlashAttribute("globalNotification", "There was no preceptron to view.");
			return "redirect:/mlp/dashboard";
		}
		model.addAttribute("mlpId", mlpId);
		model.addAttribute("runner", runner);
		return "mlp-view";
	}

	@RequestMapping(value = "/{mlpId}/remove", method = RequestMethod.GET)
	public String remove(Model model, @PathVariable String mlpId, RedirectAttributes redirect) {
		MultiLayerPerceptronRunner runner = multiLayerPerceptronService.removeTest(mlpId);
		if (runner == null) {
			redirect.addFlashAttribute("globalNotification", "There was no preceptron to remove.");
		} else {
			redirect.addFlashAttribute("globalNotification", "Successfully removed perceptron.");
		}
		return "redirect:/mlp/list";
	}

	@RequestMapping(value = "/remove/all", method = RequestMethod.GET)
	public String removeAll(Model model, RedirectAttributes redirect) {
		boolean isRunnersEmpty = multiLayerPerceptronService.removeAllRunners();
		if (isRunnersEmpty) {
			redirect.addFlashAttribute("globalNotification", "Successfully removed all perceptrons.");
		} else {
			redirect.addFlashAttribute("globalNotification", "Could not remove perceptrons.");
		}
		return "redirect:/mlp/dashboard";
	}

	@RequestMapping(value = "/{mlpId}/topology", produces = "application/json")
	@ResponseBody
	public SigmaGraphDTO topology(@PathVariable String mlpId) {
		MultiLayerPerceptronRunner runner = multiLayerPerceptronService.getMultiLayerPerceptronRunners().get(mlpId);
		return runner.getNetworkTopology();
	}

	@RequestMapping(value = "/{mlpId}/graph")
	@ResponseBody
	public double[][] graph(@PathVariable String mlpId) {
		MultiLayerPerceptronRunner runner = multiLayerPerceptronService.getMultiLayerPerceptronRunners().get(mlpId);
		return graphService.getFlotChartDoubleArray(runner);
	}

	@RequestMapping(value = "/graph/group/{dataSetInfo}")
	public String graph(Model model, @PathVariable String dataSetInfo) {
		List<MultiLayerPerceptronRunner> runners = multiLayerPerceptronService.getRunners(DataSetInfo.valueOf(dataSetInfo));
		if (runners.isEmpty()) {
			model.addAttribute("globalNotification", "There were no trained perceptrons to graph.");
		}
		model.addAttribute("runners", runners);
		model.addAttribute("dataSetInfo", dataSetInfo);
		return "mlp-graph-by-dataset";
	}

	@RequestMapping(value = "/graph/all")
	public String graphAll(Model model) {
		HashMap<DataSetInfo, List<MultiLayerPerceptronRunner>> groups = multiLayerPerceptronService.getRunnersByGroup();
		for (DataSetInfo dataSet : DataSetInfo.values()) {
			List<MultiLayerPerceptronRunner> group = groups.get(dataSet);
			model.addAttribute(dataSet.name(), graphService.getAllConvergenceErrors(group));
		}

		return "mlp-graph-all";
	}

	@RequestMapping(value = "/{mlpId}/stop")
	public String stop(@PathVariable String mlpId) {
		multiLayerPerceptronService.stopLearning(mlpId);
		return "redirect:/mlp/" + mlpId + "/view";
	}

	@RequestMapping(value = "/{mlpId}/pause")
	public String pause(@PathVariable String mlpId) {
		multiLayerPerceptronService.pauseLearning(mlpId);
		return "redirect:/mlp/" + mlpId + "/view";
	}

	@RequestMapping(value = "/{mlpId}/resume")
	public String resume(@PathVariable String mlpId) {
		multiLayerPerceptronService.resumeLearning(mlpId);
		return "redirect:/mlp/" + mlpId + "/view";
	}

	@RequestMapping(value = "/{mlpId}/test")
	public String test(@PathVariable String mlpId) {
		multiLayerPerceptronService.testPerceptron(mlpId);
		return "redirect:/mlp/" + mlpId + "/view";
	}

	@RequestMapping(value = "/save/{dataSetInfo}")
	public String save(@PathVariable String dataSetInfo, RedirectAttributes redirect) throws IOException {
		DataSetInfo dataSet = DataSetInfo.valueOf(dataSetInfo.toUpperCase());
		multiLayerPerceptronService.saveRunners(dataSet);
		redirect.addFlashAttribute("globalNotification", "Successfully saved perceptrons trained on  " + i18n.getMessage("data.set.name." + dataSet.name()) + " data set.");
		return "redirect:/mlp/dashboard";
	}

	@RequestMapping(value = "/retrieve/{dataSetInfo}")
	public String retrieve(@PathVariable String dataSetInfo, RedirectAttributes redirect) throws IOException, ClassNotFoundException {
		DataSetInfo dataSet = DataSetInfo.valueOf(dataSetInfo.toUpperCase());
		multiLayerPerceptronService.retrieveRunners(dataSet);
		redirect.addFlashAttribute("globalNotification", "Successfully retrieved perceptrons trained on " + i18n.getMessage("data.set.name." + dataSet.name()) + " data set.");
		return "redirect:/mlp/dashboard";
	}

	@RequestMapping(value = "/save/all")
	public String save(RedirectAttributes redirect) {

		try {
			multiLayerPerceptronService.saveRunners();
			redirect.addFlashAttribute("globalNotification", "Successfully saved all perceptrons");
		} catch (IOException io) {
			LOGGER.error("Saving all perceptrons failed: " + io.getMessage(), io);
			redirect.addFlashAttribute("globalWarning", "Saving perceptrons failed. IOException occurred. Reason: " + io.getMessage());
		}
		return "redirect:/mlp/dashboard";
	}

	@RequestMapping(value = "/retrieve/all")
	public String retrieve() throws IOException, ClassNotFoundException {
		multiLayerPerceptronService.retrieveRunners();
		return "redirect:/mlp/dashboard";
	}
}

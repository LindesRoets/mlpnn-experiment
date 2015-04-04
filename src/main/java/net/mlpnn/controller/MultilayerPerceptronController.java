package net.mlpnn.controller;

import java.util.HashMap;
import java.util.List;
import javax.validation.Valid;
import net.mlpnn.constants.ResourcePath;
import net.mlpnn.dto.NetworkStatusDTO;
import net.mlpnn.dto.SigmaGraphDTO;
import net.mlpnn.enums.DataSetInfo;
import net.mlpnn.form.MultilayerPercetpronParametersForm;
import net.mlpnn.service.GraphService;
import net.mlpnn.service.MultiLayerPerceptronRunner;
import net.mlpnn.service.MultiLayerPerceptronService;
import org.neuroph.nnet.MultiLayerPerceptron;
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
public class MultilayerPerceptronController {

    @Autowired
    MultiLayerPerceptronService multiLayerPerceptronService;

    @Autowired
    GraphService graphService;

    @RequestMapping(value = "/train", method = RequestMethod.POST)
    public String train(Model model, @Valid @ModelAttribute(value = "mlpForm") MultilayerPercetpronParametersForm form, BindingResult result) {
        if (!result.hasErrors()) {
            multiLayerPerceptronService.startLearning(form);
            return "redirect:/mlp/dashboard";
        } else {
            model.addAttribute("datasets", DataSetInfo.values());
            return "mlp-create";
        }
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboard(Model model) throws InterruptedException {
        List<NetworkStatusDTO> statuses = multiLayerPerceptronService.getPerceptronStatuses();
        model.addAttribute("statuses", statuses);
        return "mlp-dashboard";
    }

    @RequestMapping(value = ResourcePath.MLP_CREATE, method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("mlpForm", new MultilayerPercetpronParametersForm());
        model.addAttribute("datasets", DataSetInfo.values());
        return "mlp-create";
    }

    @RequestMapping(value = "/view/{mlpId}", method = RequestMethod.GET)
    public String view(Model model, @PathVariable Long mlpId, RedirectAttributes redirect) {
        MultiLayerPerceptronRunner runner = multiLayerPerceptronService.getMultiLayerPerceptronRunners().get(mlpId);
        if (runner == null) {
            redirect.addFlashAttribute("globalNotification", "There was no preceptron to view");
            return "redirect:/mlp/dashboard";
        }
        model.addAttribute("mlpId", mlpId);
        model.addAttribute("runner", runner);
        return "mlp-view";
    }

    @RequestMapping(value = "/remove/{mlpId}", method = RequestMethod.GET)
    public String remove(Model model, @PathVariable Long mlpId, RedirectAttributes redirect) {
        MultiLayerPerceptron perceptron = multiLayerPerceptronService.removeTest(mlpId);
        if (perceptron == null) {
            redirect.addFlashAttribute("globalNotification", "There was no preceptron to remove");
        } else {
            redirect.addFlashAttribute("globalNotification", "Successfully removed perceptron");
        }
        return "redirect:/mlp/dashboard";
    }

    @RequestMapping(value = "/topology/{mlpId}", produces = "application/json")
    @ResponseBody
    public SigmaGraphDTO topology(@PathVariable Long mlpId) {
        MultiLayerPerceptronRunner runner = multiLayerPerceptronService.getMultiLayerPerceptronRunners().get(mlpId);
        return runner.getNetworkTopology();
    }

    @RequestMapping(value = "/graph/{mlpId}/{graphType}")
    @ResponseBody
    public double[][] graph(@PathVariable Long mlpId, @PathVariable String graphType) {
        MultiLayerPerceptronRunner runner = multiLayerPerceptronService.getMultiLayerPerceptronRunners().get(mlpId);
        return graphService.getFlotChartDoubleArray(runner);
    }

    @RequestMapping(value = "/graph/all")
    public String graphAll(Model model) {
        HashMap<Long, MultiLayerPerceptronRunner> runners = multiLayerPerceptronService.getMultiLayerPerceptronRunners();
        model.addAttribute("series", graphService.getAllConvergenceErrors(runners));
        return "mlp-view-all";
    }

    @RequestMapping(value = "/stop/{mlpId}")
    public String stop(@PathVariable Long mlpId) {
        multiLayerPerceptronService.stopLearning(mlpId);
        return "redirect:/mlp/view/" + mlpId;
    }

    @RequestMapping(value = "/pause/{mlpId}")
    public String pause(@PathVariable Long mlpId) {
        multiLayerPerceptronService.pauseLearning(mlpId);
        return "redirect:/mlp/view/" + mlpId;
    }

    @RequestMapping(value = "/resume/{mlpId}")
    public String resume(@PathVariable Long mlpId) {
        multiLayerPerceptronService.resumeLearning(mlpId);
        return "redirect:/mlp/view/" + mlpId;
    }
}

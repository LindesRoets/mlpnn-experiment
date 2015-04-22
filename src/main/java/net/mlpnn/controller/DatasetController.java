package net.mlpnn.controller;

import java.io.IOException;
import net.mlpnn.enums.DataSetInfo;
import net.mlpnn.service.DataSetService;
import net.mlpnn.service.MultiLayerPerceptronService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Lindes Roets
 */
@Controller
@RequestMapping("/dataset")
public class DatasetController extends BaseController{

	private final Logger LOGGER = LoggerFactory.getLogger(DatasetController.class);

	@Autowired
	MultiLayerPerceptronService multiLayerPerceptronService;

	@Autowired
	public DataSetService dataSetService;

	@RequestMapping("/download/all")
	public String downloadDataSets(RedirectAttributes redirect) {

		try {
			dataSetService.downloadDataSets();
			redirect.addFlashAttribute("globalNotification", "Successfully downloaded all the datasets!");
		} catch (IOException io) {
			LOGGER.error("Download all dataset call from controller failed: " + io.getMessage(), io);
			redirect.addFlashAttribute("globalNotification", "Downloading the datasets failed. IOException occurred. Reason: " + io.getMessage());
		}

		return "redirect:/dataset/dashboard";

	}

	@RequestMapping("/download/{dataSetName}")
	public String downloadDataSet(@PathVariable("dataSetName") String dataSetName, RedirectAttributes redirect) throws IOException {
		dataSetService.downloadDataSet(DataSetInfo.valueOf(dataSetName.toUpperCase()));
		redirect.addFlashAttribute("globalNotification", "Successfully downloaded " + dataSetName);
		return "redirect:/dataset/dashboard";
	}

	@RequestMapping("/dashboard")
	public String show(Model model) {
		model.addAttribute("datasets", DataSetInfo.values());
		return "dataset-dashboard";
	}

	@RequestMapping("/refactor/{dataSetName}")
	public String refactorDataSet(@PathVariable("dataSetName") String dataSetName, RedirectAttributes redirect) throws IOException {
		dataSetService.preprocessDataSet(DataSetInfo.valueOf(dataSetName.toUpperCase()));
		redirect.addFlashAttribute("globalNotification", "Successfully refactored " + dataSetName);
		return "redirect:/dataset/dashboard";
	}

}

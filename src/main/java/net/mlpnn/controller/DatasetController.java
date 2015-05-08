package net.mlpnn.controller;

import java.io.IOException;
import net.mlpnn.enums.DataSetInfo;
import net.mlpnn.service.DataSetService;
import net.mlpnn.service.I18NService;
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
public class DatasetController extends BaseController {

	private final Logger LOGGER = LoggerFactory.getLogger(DatasetController.class);

	@Autowired
	MultiLayerPerceptronService multiLayerPerceptronService;

	@Autowired
	public DataSetService dataSetService;

	@Autowired
	I18NService i18n;

	@RequestMapping("/download/all")
	public String downloadDataSets(RedirectAttributes redirect) {

		try {
			for (DataSetInfo info : DataSetInfo.values()) {
				dataSetService.downloadDataSet(info);
				dataSetService.createTestAndValidationDataForRegressor(info);
			}
			redirect.addFlashAttribute("globalNotification", "Successfully downloaded all the datasets!");
		} catch (IOException io) {
			LOGGER.error("Download all dataset call from controller failed: " + io.getMessage(), io);
			redirect.addFlashAttribute("globalNotification", "Downloading the datasets failed. IOException occurred. Reason: " + io.getMessage());
		}

		return "redirect:/dataset/dashboard";

	}

	@RequestMapping("/download/{dataSetName}")
	public String downloadDataSet(@PathVariable("dataSetName") String dataSetName, RedirectAttributes redirect) {
		redirect.addFlashAttribute("globalNotification", "Successfully downloaded "  + i18n.getMessage("data.set.name." +dataSetName));
		try {

			dataSetService.downloadDataSet(DataSetInfo.valueOf(dataSetName.toUpperCase()));
			dataSetService.createTestAndValidationDataForRegressor(DataSetInfo.valueOf(dataSetName.toUpperCase()));
		} catch (IOException ioe) {
			LOGGER.error("Downloading data set failed: dataSetName: " + dataSetName, ioe);
			redirect.addFlashAttribute("globalNotification", "Could not download " + i18n.getMessage("data.set.name." +dataSetName) + ". The reported problem is: " + ioe.getMessage());

		}

		return "redirect:/dataset/dashboard";
	}

	@RequestMapping("/dashboard")
	public String showDashboard(Model model) {
		model.addAttribute("datasets", DataSetInfo.values());
		return "dataset-dashboard";
	}

}

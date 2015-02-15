package net.mlpnn.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import net.mlpnn.dto.SigmaGraphDTO;
import net.mlpnn.enums.DataSetInfo;
import net.mlpnn.form.MultilayerPercetpronParametersForm;
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
@RequestMapping("/mlp")
public class MultilayerPerceptronController {

    @Autowired
    MultiLayerPerceptronService multiLayerPerceptronService;

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
    public String dashboard(Model model) {
        List<Long> mlpIds = null;
        if (multiLayerPerceptronService.getMultiLayerPerceptronRunners() != null && multiLayerPerceptronService.getMultiLayerPerceptronRunners().size() > 0) {
            Set<Long> ids = multiLayerPerceptronService.getMultiLayerPerceptronRunners().keySet();
            Long[] idss = ids.toArray(new Long[ids.size()]);
            mlpIds = Arrays.asList(idss);
            Collections.sort(mlpIds);
        }
        model.addAttribute("mlpIds", mlpIds);
        return "mlp-dashboard";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
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

}

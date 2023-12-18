package universe.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import universe.pojo.KubernetesResource;
import universe.service.KubernetesCreatorService;
import universe.service.KubernetesUpdaterService;

@Controller
@Slf4j
@RequestMapping("/kubernetes")
public class KubernetesController {
    @Autowired
    KubernetesCreatorService kubernetesCreatorService;
    @Autowired
    KubernetesUpdaterService kubernetesUpdaterService;
    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("kubernetesResource", new KubernetesResource());
        return "form";
    }

    @PostMapping("/createResource")
    public String createResource(KubernetesResource kubernetesResource) {
        switch (kubernetesResource.getType()) {
            case "Namespace" -> kubernetesCreatorService.createNameSpace(kubernetesResource.getName());
            case "Pod" ->
                    kubernetesCreatorService.createPod(kubernetesResource.getName(), kubernetesResource.getPodContainerName(), kubernetesResource.getDockerImage());
            case "Job" -> kubernetesCreatorService.createJob(kubernetesResource.getName(),
                    kubernetesResource.getJobContainerName(),
                    kubernetesResource.getNamespace()
                    , kubernetesResource.getJobDockerImage());
            case "Deployment" -> kubernetesCreatorService.createDeployment(kubernetesResource.getName()
                    , kubernetesResource.getDeploymentNamespace()
                    , kubernetesResource.getDeploymentContainerName()
                    , kubernetesResource.getDeploymentDockerImage()
                    , kubernetesResource.getDeploymentLabels()
                    , kubernetesResource.getDeploymentMatchLabels());
            default -> log.error("Something went wrong");
        }

        return "redirect:/";
    }

    // Update Namespace Form
    @GetMapping("/updateNamespace/{namespaceName}")
    public String showUpdateNamespaceForm(@PathVariable String namespaceName, Model model) {
        model.addAttribute("namespaceName", namespaceName);
        model.addAttribute("newDescription", ""); // Default value, adjust as needed
        return "updateNamespaceForm";
    }

    @PostMapping("/updateNamespace")
    public String updateNamespace(@RequestParam String namespaceName, @RequestParam String newDescription) {
        kubernetesUpdaterService.updateNamespace(namespaceName, newDescription);
        return "redirect:/kubernetes";
    }

    // Update Pod Form
    @GetMapping("/updatePod/{namespace}/{podName}")
    public String showUpdatePodForm(@PathVariable String namespace, @PathVariable String podName, Model model) {
        model.addAttribute("namespace", namespace);
        model.addAttribute("podName", podName);
        model.addAttribute("newImage", ""); // Default value, adjust as needed
        return "updatePodForm";
    }

    @PostMapping("/updatePod")
    public String updatePod(@RequestParam String namespace, @RequestParam String podName, @RequestParam String newImage) {
        kubernetesUpdaterService.updatePod(podName, namespace, newImage);
        return "redirect:/kubernetes";
    }

    // Update Job Form
    @GetMapping("/updateJob/{namespace}/{jobName}")
    public String showUpdateJobForm(@PathVariable String namespace, @PathVariable String jobName, Model model) {
        model.addAttribute("namespace", namespace);
        model.addAttribute("jobName", jobName);
        model.addAttribute("newImage", ""); // Default value, adjust as needed
        return "updateJobForm";
    }

    @PostMapping("/updateJob")
    public String updateJob(@RequestParam String namespace, @RequestParam String jobName, @RequestParam String newImage) {
        kubernetesUpdaterService.updateJob(jobName, namespace, newImage);
        return "redirect:/kubernetes";
    }

    // Update Deployment Form
    @GetMapping("/updateDeployment/{namespace}/{deploymentName}")
    public String showUpdateDeploymentForm(@PathVariable String namespace, @PathVariable String deploymentName, Model model) {
        model.addAttribute("namespace", namespace);
        model.addAttribute("deploymentName", deploymentName);
        model.addAttribute("newImage", ""); // Default value, adjust as needed
        return "updateDeploymentForm";
    }

    @PostMapping("/updateDeployment")
    public String updateDeployment(@RequestParam String namespace, @RequestParam String deploymentName, @RequestParam String newImage) {
        kubernetesUpdaterService.updateDeployment(deploymentName, namespace, newImage);
        return "redirect:/kubernetes";
    }
}


package universe.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import universe.pojo.KubernetesResource;
import universe.service.KubernetesCreatorService;

@Controller
@Slf4j
public class KubernetesController {
    @Autowired
    KubernetesCreatorService kubernetesCreatorService;

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
}


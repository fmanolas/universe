package universe.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import universe.pojo.KubernetesResource;
import universe.service.KubernetesCreatorService;
import universe.service.KubernetesDeleteService;
import universe.service.KubernetesUpdaterService;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/kubernetes")
public class KubernetesController {
    @Autowired
    KubernetesCreatorService kubernetesCreatorService;
    @Autowired
    KubernetesUpdaterService kubernetesUpdaterService;
    @Autowired
    KubernetesDeleteService kubernetesDeleteService;

    @GetMapping("/createResources")
    public String showCreateResourcePage() {
        return "createResource";
    }
    @GetMapping("/createNamespace")
    public String showCreateNamespaceForm(Model model) {
        model.addAttribute("kubernetesResource", new KubernetesResource());
        return "createNamespace";
    }
    @GetMapping("/createPod")
    public String showCreatePodForm(Model model) {
        model.addAttribute("kubernetesResource", new KubernetesResource());
        return "createPod";
    }
    @GetMapping("/createJob")
    public String showCreateJobForm(Model model) {
        model.addAttribute("kubernetesResource", new KubernetesResource());
        return "createJob";
    }

    @GetMapping("/createDeployment")
    public String showCreateDeploymentForm(Model model) {
        model.addAttribute("kubernetesResource", new KubernetesResource());
        return "createDeployment";
    }
    @PostMapping("/createNamespace")
    public String createNamespace(KubernetesResource kubernetesResource) {
        kubernetesCreatorService.createNameSpace(kubernetesResource.getName());
        return "redirect:/index";
    }

    @PostMapping("/createPod")
    public String createPod(KubernetesResource kubernetesResource) {
        kubernetesCreatorService.createPod(
                kubernetesResource.getName(),
                kubernetesResource.getPodContainerName(),
                kubernetesResource.getDockerImage(),
                kubernetesResource.getNamespaceForPod()
        );
        return "redirect:/index";
    }

    @PostMapping("/createJob")
    public String createJob(KubernetesResource kubernetesResource) {
        kubernetesCreatorService.createJob(
                kubernetesResource.getName(),
                kubernetesResource.getJobContainerName(),
                kubernetesResource.getNamespace(),
                kubernetesResource.getJobDockerImage()
        );
        return "redirect:/index";
    }

    @PostMapping("/createDeployment")
    public String createDeployment(KubernetesResource kubernetesResource) {
        kubernetesCreatorService.createDeployment(
                kubernetesResource.getName(),
                kubernetesResource.getDeploymentNamespace(),
                kubernetesResource.getDeploymentContainerName(),
                kubernetesResource.getDeploymentDockerImage(),
                kubernetesResource.getDeploymentLabels(),
                kubernetesResource.getDeploymentMatchLabels()
        );
        return "redirect:/index";
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
        return "redirect:/index";
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
        return "redirect:/index";
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
        return "redirect:/index";
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
        return "redirect:/index";
    }

    @GetMapping("/pod/{namespace}/{podName}")
    public String showDeletePodForm(@PathVariable String namespace, @PathVariable String podName, Model model) {
        model.addAttribute("namespace", namespace);
        model.addAttribute("podName", podName);
        return "deletePodForm";
    }

    @PostMapping("/delete/pod")
    public String deletePod(@RequestParam String namespace, @RequestParam String podName) {
        kubernetesDeleteService.deletePod(podName, namespace);
        return "redirect:/index";
    }

    // Delete Namespace
    @GetMapping("/delete/namespace/{namespaceName}")
    public String showDeleteNamespaceForm(@PathVariable String namespaceName, Model model) {
        model.addAttribute("namespaceName", namespaceName);
        return "deleteNamespaceForm";
    }

    @PostMapping("/delete/namespace")
    public String deleteNamespace(@RequestParam String namespaceName) {
        kubernetesDeleteService.deleteNamespace(namespaceName);
        return "redirect:/index";
    }

    // Delete Job
    @GetMapping("/delete/job/{namespace}/{jobName}")
    public String showDeleteJobForm(@PathVariable String namespace, @PathVariable String jobName, Model model) {
        model.addAttribute("namespace", namespace);
        model.addAttribute("jobName", jobName);
        return "deleteJobForm";
    }

    @PostMapping("/delete/job")
    public String deleteJob(@RequestParam String namespace, @RequestParam String jobName) {
        kubernetesDeleteService.deleteJob(jobName, namespace);
        return "redirect:/index";
    }

    // Delete Deployment
    @GetMapping("/delete/deployment/{namespace}/{deploymentName}")
    public String showDeleteDeploymentForm(@PathVariable String namespace, @PathVariable String deploymentName, Model model) {
        model.addAttribute("namespace", namespace);
        model.addAttribute("deploymentName", deploymentName);
        return "deleteDeploymentForm";
    }

    @PostMapping("/delete/deployment")
    public String deleteDeployment(@RequestParam String namespace, @RequestParam String deploymentName) {
        kubernetesDeleteService.deleteDeployment(deploymentName, namespace);
        return "redirect:/index";
    }


    @GetMapping("/namespaces")
    public String showAllNamespaces(Model model) {
        // Add logic to fetch all namespaces from your service
        List<String> allNamespaces = kubernetesUpdaterService.retrieveAllNamespaceNames();
        model.addAttribute("allNamespaces", allNamespaces);
        return "allNamespaces";
    }

    @GetMapping("/pods")
    public String showAllPods(Model model){
        Map<String,String> allPods = kubernetesUpdaterService.retrieveAllpods();
        model.addAttribute("allPods",allPods);
        return "allPods";
    }

    @GetMapping("/jobs")
    public String showAllJobs(Model model){
        Map<String,String> allJobs = kubernetesUpdaterService.retrieveAllJobs();
        model.addAttribute("allJobs",allJobs);
        return "allJobs";
    }

    @GetMapping("/deployments")
    public String showAllDeployments(Model model){
        Map<String,String> allDeployments = kubernetesUpdaterService.retrieveAllDeployments();
        model.addAttribute("allDeployments",allDeployments);
        return "allDeployments";
    }
}


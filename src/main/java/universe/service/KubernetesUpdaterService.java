package universe.service;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentList;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.api.model.batch.v1.JobList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static universe.config.KubernetesConfig.kubernetesClient;

@Service
@Slf4j
public class KubernetesUpdaterService {

    public void updateNamespace(String namespaceName, String newDescription) {
        try (var kubclient = kubernetesClient()) {
            Resource<Namespace> namespaceResource = kubclient.namespaces().withName(namespaceName);

            // Retrieve the existing Namespace
            Namespace existingNamespace = namespaceResource.get();

            if (existingNamespace != null) {
                // Update the Namespace
                existingNamespace.getMetadata().setAnnotations(
                        existingNamespace.getMetadata().getAnnotations() == null
                                ? Map.of("description", newDescription)
                                : existingNamespace.getMetadata().getAnnotations());

                namespaceResource.createOrReplace(existingNamespace);
                log.info("Namespace with name "+namespaceName+" has been updated successfully");
            } else {
                log.error("Given namespace doesnt exist");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    public void updatePod(String podName, String namespace, String newImage) {
        try (var kubclient = kubernetesClient()) {
            Resource<Pod> podResource = kubclient.pods().inNamespace(namespace).withName(podName);

            // Retrieve the existing Pod
            Pod existingPod = podResource.get();

            if (existingPod != null) {
                // Update the Pod
                existingPod.getSpec().getContainers().get(0).setImage(newImage);

                podResource.createOrReplace(existingPod);
                log.info("Pod has been successfully updated");
            } else {
                log.error("Given pod doesnt exist");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    public void updateJob(String jobName, String namespace, String newImage) {
        try (var kubclient = kubernetesClient()) {
            Resource<Job> jobResource = kubclient.batch().v1().jobs().inNamespace(namespace).withName(jobName);

            // Retrieve the existing Job
            Job existingJob = jobResource.get();

            if (existingJob != null) {
                // Update the Job
                existingJob.getSpec().getTemplate().getSpec().getContainers().get(0).setImage(newImage);

                jobResource.createOrReplace(existingJob);
                log.info("Job has been updated successfully");
            } else {
                log.error("Given Job doesnt exist");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    public void updateDeployment(String deploymentName, String namespace, String newImage) {
        try (var kubclient = kubernetesClient()) {
            Resource<Deployment> deploymentResource = kubclient.apps().deployments().inNamespace(namespace).withName(deploymentName);

            // Retrieve the existing Deployment
            Deployment existingDeployment = deploymentResource.get();

            if (existingDeployment != null) {
                // Update the Deployment
                existingDeployment.getSpec().getTemplate().getSpec().getContainers().get(0).setImage(newImage);

                deploymentResource.createOrReplace(existingDeployment);
                log.info("Deployment has been successfully updated");
            } else {
                log.error("Given Deployment doesnt exist");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    public List<String> retrieveAllNamespaceNames() {
        try (var kubclient = kubernetesClient()) {
            // List all namespaces
            NamespaceList namespaceList = kubclient.namespaces().list();
            return namespaceList.getItems().stream().map(it -> it.getMetadata().getName()).toList();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return  List.of();
    }

    public Map<String,String> retrieveAllpods(){
        Map<String,String> map = new HashMap<>();
        try (KubernetesClient client = kubernetesClient()) {
            // List all pods in all namespaces
            PodList podList = client.pods().inAnyNamespace().list();

            // Iterate through the list of pods
            for (Pod pod : podList.getItems()) {
                map.put(pod.getMetadata().getNamespace(),pod.getMetadata().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public Map<String,String> retrieveAllDeployments(){
        Map<String,String> map = new HashMap<>();
        try (KubernetesClient client = kubernetesClient()) {
            // List all pods in all namespaces
            DeploymentList depList = client.apps().deployments().inAnyNamespace().list();

            // Iterate through the list of pods
            for (Deployment deployment : depList.getItems()) {
                map.put(deployment.getMetadata().getNamespace(),deployment.getMetadata().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public Map<String,String> retrieveAllJobs(){
        Map<String,String> map = new HashMap<>();
        try (KubernetesClient client = kubernetesClient()) {
            // List all pods in all namespaces
            JobList depList = client.batch().jobs().inAnyNamespace().list();

            // Iterate through the list of pods
            for (Job job : depList.getItems()) {
                map.put(job.getMetadata().getNamespace(),job.getMetadata().getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}

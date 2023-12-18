package universe.service;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.api.model.batch.v1.JobBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class KubernetesCreatorService {
    private final KubernetesClient kubernetesClient;

    @Autowired
    public KubernetesCreatorService(KubernetesClient client) {
        this.kubernetesClient = client;
    }

    public void createNameSpace(String nameSpaceName){
            // Create a new Namespace object
            Namespace namespace = new Namespace();

            // Set the metadata for the namespace
            ObjectMeta metadata = new ObjectMeta();
            metadata.setName(nameSpaceName); // Set the desired namespace name
            namespace.setMetadata(metadata);

            // Create the namespace
            this.kubernetesClient.namespaces().create(namespace);
            log.info("Namespace created successfully.");
    }

    public void createPod(String podName,String containerName,String image){

            // Create a new Pod object
            Pod pod = new PodBuilder()
                    .withNewMetadata()
                    .withName(podName) // Set the desired pod name
                    .endMetadata()
                    .withNewSpec()
                    .withContainers(
                            new ContainerBuilder()
                                    .withName(containerName)
                                    .withImage(image)
                                    .build()
                    )
                    .endSpec()
                    .build();

            // Create the pod
            this.kubernetesClient.pods().inNamespace("default").create(pod);

            log.info("Pod created successfully.");
    }

    public void createJob(String jobName,String containerName,String nameSpace,String image){
            // Create a new Job object
            Job job = new JobBuilder()
                    .withNewMetadata()
                    .withName(jobName) // Set the desired job name
                    .endMetadata()
                    .withNewSpec()
                    .withNewTemplate()
                    .withNewSpec()
                    .addNewContainer()
                    .withName(containerName)
                    .withImage(image) // Set the desired container image
                    .endContainer()
                    .endSpec()
                    .endTemplate()
                    .endSpec()
                    .build();

            // Create the job
            this.kubernetesClient.batch().v1().jobs().inNamespace(nameSpace).create(job);

            log.info("Job created successfully.");
    }

    public void createDeployment(String deploymentName,String namespace,String containerName,String image,String matchLabel,String label){
            // Create a new Deployment object
            Deployment deployment = new DeploymentBuilder()
                    .withNewMetadata()
                    .withName(deploymentName) // Set the desired deployment name
                    .endMetadata()
                    .withNewSpec()
                    .withReplicas(1) // Set the desired number of replicas
                    .withNewSelector()
                    .withMatchLabels(Map.of("app", matchLabel))
                    .endSelector()
                    .withNewTemplate()
                    .withNewMetadata()
                    .withLabels(Map.of("app", label))
                    .endMetadata()
                    .withNewSpec()
                    .addNewContainer()
                    .withName(containerName)
                    .withImage(image) // Set the desired container image
                    .endContainer()
                    .endSpec()
                    .endTemplate()
                    .endSpec()
                    .build();

            // Create the deployment
            this.kubernetesClient.apps().deployments().inNamespace(namespace).create(deployment);

            log.info("Deployment created successfully.");
    }
}

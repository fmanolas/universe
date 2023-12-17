package universe.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KubernetesResource {
    private String type;
    private String name;
    private String dockerImage; // New field for Pod
    private String podContainerName; // New field for Pod
    private String namespace; // For Job
    private String jobDockerImage; // For Job
    private String jobContainerName; // For Job
    private String deploymentNamespace; // For Deployment
    private String deploymentDockerImage; // For Deployment
    private String deploymentContainerName; // For Deployment
    private String deploymentLabels; // For Deployment
    private String deploymentMatchLabels; // For Deployment
}
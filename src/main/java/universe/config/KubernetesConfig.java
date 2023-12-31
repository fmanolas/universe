package universe.config;

import io.fabric8.kubernetes.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class KubernetesConfig {

    public static KubernetesClient kubernetesClient() {
        return new KubernetesClientBuilder().build();
    }
}
package universe.client;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.util.Config;

import java.io.IOException;

public class KubernetesClient {
    ApiClient client = Config.defaultClient();

    private static KubernetesClient kubernetesClient;

    public KubernetesClient() throws IOException {
    }

    public static KubernetesClient getInstance() throws IOException {
        if(kubernetesClient==null){
            kubernetesClient=new KubernetesClient();
        }
        return kubernetesClient;
    }
}

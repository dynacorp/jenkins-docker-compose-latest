package jenkins.plugins.docker_compose;

import hudson.DescriptorExtensionList;
import hudson.model.Descriptor;
import jenkins.model.Jenkins;

/**
 * DockerComposeCommandOptionDescriptor
 *
 * @author <a href="mailto:olophaayomide@gmail.com">Ayomide Olopha</a>
 */
public abstract class DockerComposeCommandOptionDescriptor extends Descriptor<DockerComposeCommandOption> {

    public static DescriptorExtensionList<DockerComposeCommandOption, DockerComposeCommandOptionDescriptor> all() {

        try {
            return Jenkins.getInstance().getDescriptorList(DockerComposeCommandOption.class);
        }
        catch (NullPointerException ex) {
            throw ex;
        }
    }

}
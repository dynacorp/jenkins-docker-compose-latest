package jenkins.plugins.docker_compose;

import java.io.File;
import java.io.IOException;

import org.kohsuke.stapler.DataBoundConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.Launcher.ProcStarter;
import hudson.Proc;
import hudson.model.Run;
import hudson.model.TaskListener;

/**
 * StartAllService
 *
 * @author <a href="mailto:olophaayomide@gmail.com">Ayomide Olopha</a>
 */
public class StartAllServices extends DockerComposeCommandOption {

	private static final long serialVersionUID = 1L;

	// Logger
	private static final Logger LOGGER = LoggerFactory.getLogger(StartAllServices.class);
	
	// Constants
	private static final String DISPLAY_NAME = "Start all services";

	// Form parameters
	// TODO: Add service+scale list

	@DataBoundConstructor
	public StartAllServices() {
		super();
	}

	@Override
	public int execute(Run<?, ?> build, FilePath workspace, Launcher launcher, TaskListener listener, DockerComposeBuilder dockerComposeStep)
			throws DockerComposeCommandException {
		
		try {
            // Initialize command
            StringBuilder cmd = new StringBuilder();
            cmd.append("docker compose");

            // Set compose file
            String composeFile = dockerComposeStep.getUseCustomDockerComposeFile() ? dockerComposeStep.getDockerComposeFile() : "docker-compose.yml";
            LOGGER.info("Using Docker Compose file '{}'", composeFile);
            if(new File(composeFile).exists()) {
            	cmd.append(" -f " + composeFile);
            } else {
            	// Add path relative to workspace
            	if(launcher.isUnix()) {
            		cmd.append(" -f " + workspace.toURI().getPath().replaceAll("/$", "") + "/" + composeFile);
            	} else {
            		cmd.append(" -f " + workspace.toURI().getPath().replaceAll("^/|/$", "") + "/" + composeFile);
            	}
            }
            cmd.append(" up -d");

            // Launch command
            LOGGER.info("Starting all services");
            ProcStarter ps = launcher.new ProcStarter();
            ps.cmdAsSingleString(cmd.toString()).stdout(listener);
            Proc proc = launcher.launch(ps);

            return proc.join();
        }
        catch (IOException | InterruptedException ex) {

            throw new DockerComposeCommandException(ex);
        }
		
	}

	@Extension
	public static final class DescriptorImpl extends DockerComposeCommandOptionDescriptor {

		@Override
		public String getDisplayName() {

			return DISPLAY_NAME;
		}
	}
}
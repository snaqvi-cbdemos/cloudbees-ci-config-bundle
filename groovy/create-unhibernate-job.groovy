import jenkins.model.*;
import java.util.logging.Logger;

Logger logger = Logger.getLogger("create-unhibernate-job.groovy");

def jenkins = Jenkins.instance
def name = "unhibernate"

def job = jenkins.getItem(name)
if (job != null) {
  logger.info("job $name already existed so deleting")
  job.delete()
}

//11-12 UTC Monday thru Friday, update for your timezone
//this would be 6-7am EST M-F
def cronSchedule = "H 11 * * 1-5"

//unhibernate job
def unhibernateJobXml = """
<project>
  <description></description>
  <keepDependencies>false</keepDependencies>
  <properties>
    <jenkins.model.BuildDiscarderProperty>
      <strategy class="hudson.tasks.LogRotator">
        <daysToKeep>-1</daysToKeep>
        <numToKeep>2</numToKeep>
        <artifactDaysToKeep>-1</artifactDaysToKeep>
        <artifactNumToKeep>-1</artifactNumToKeep>
      </strategy>
    </jenkins.model.BuildDiscarderProperty>
  </properties>
  <scm class="hudson.scm.NullSCM"/>
  <canRoam>true</canRoam>
  <disabled>false</disabled>
  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>
  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>
  <triggers>
    <hudson.triggers.TimerTrigger>
      <spec>${cronSchedule}</spec>
    </hudson.triggers.TimerTrigger>
  </triggers>
  <concurrentBuild>false</concurrentBuild>
  <builders/>
  <publishers/>
  <buildWrappers/>
</project>
"""

def p = jenkins.createProjectFromXML(name, new ByteArrayInputStream(unhibernateJobXml.getBytes("UTF-8")));

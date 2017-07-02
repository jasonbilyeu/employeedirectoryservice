node {
  // Mark the code checkout 'stage'....
  stage 'Stage Checkout'

  // Checkout code from repository and update any submodules
  checkout scm 

  stage 'Stage Build'

  //branch name from Jenkins environment variables
  echo "My branch is: ${env.BRANCH_NAME}"
  
  //build your gradle flavor, passes the current build number as a parameter to gradle
  sh "./gradlew clean build"

  stage 'Stage Archive'
  junit 'build/test-results/**/*.xml'
  archiveArtifacts artifacts: 'build/reports/**/*,build/test-results/**/*'
  step( [ $class: 'JacocoPublisher' ] )
}

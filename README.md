From the default Gatling maven archetype

Contains two simulations executed on Squash TM.

Squash : 

Login -> Requirement Workspace -> New Requirement with the payload loaded from a .json in the resources folder.

This simulation was generated from the recorder without much changes to the script. The scenario is also quite simple.

tmScenarios :

3 scenarios are in this simulation.

Login -> Requirement Workspace -> New Requirement (payload as a StringBody)

Login -> Administration -> Create a new user

Login -> Testcase Workspace -> Click on the first test case

_________________

Do keep in mind that you need to change the baseUrl and that your Squash TM needs to have a project created with the first Id.

tmScenarios also need to have a few test cases available.

YOu can execute this simulation with :

 mvn gatling:test -Dgatling.simulationClass={Squash||tmScenarios}

It's possible to execute multiple tests if the property runMultipleSimulations is set to true
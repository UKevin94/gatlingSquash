From the default Gatling maven archetype

Contains one simulation executed on Squash TM.

The scenario is the following :

Login -> Requirement Workspace -> New Requirement with the payload loaded from a .json in the resources folder.

Do keep in mind that you need to change the baseUrl and that your Squash TM needs to have a project created with the first Id.

YOu can execute this simulation with :

 mvn gatling:test

However, it'll not work if at least 2 simulations are in the project

It's possible to execute multiple tests if the property runMultipleSimulations is set to true

If your project contains multiple simulations and you want to specify which one needs to be used, you can add the property simulationClass.

For example :

    mvn gatling:test -Dgatling.simulationClass=computerdatabase.BasicSimulation
    
    or
    
    mvn gatling:test -Dgatling.simulationClass=Squash


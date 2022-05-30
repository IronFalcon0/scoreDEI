## scoreDEI
===================================================================================
    Departamento Eng. Informatica - FCTUC
    Sistemas Distribuídos - 2021/2022
    ................................................
    Rodrigo Francisco Ferreira \ nº2019220060
    Sofia Botelho Vieira Alves \ nº2019227240
    ................................................
===================================================================================

:::::::::::::::::::::::: scoreDEI: Resultados desportivos em direto ::::::::::::::::::

## Requirements:
    - Docker
    - Visual Studio Code


## How to Run
	-> To run the program you should first unzip the folder scoreDEI.zip.
    
    -> You should have docker running. To install:   
            https://docs.docker.com/desktop/windows/install/

    

    -> Then open in a container the subfolder scoresDei+webservices.

    -> After it finishes to load all the information necessary, open a zsh terminal and run the following command:

            ./mvnw spring-boot:run

    This command should start the scoresDEI server and create the database. 
    
    -> After that, open a tab on the following URL:

            http://localhost:8080/


    It's recommended to open first this URL to add some default information to the database like the client roles and some default users. This addictions only happen one time and are there to give the user a way to login the first time after turning the server on.

    The default users are the following:
        username: admin     password: admin     role: admin
        username: user      password: user      role: user


## Throubleshooting

    If there are any bugs you may try to rebuild the container using the following command:

            mvn clean install spring-boot:repackage

===================================================================================
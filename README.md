"# RPC2023" 
En el primer proyecto se solicita programar una aplicación usando RPC (RMI). Se debe contar con varios clientes que envían solicitudes a un servidor.

El servidor debe tener una máquina de estado (SM) muy sencilla que ejecute las solicitudes de los clientes.

(Schneider, 1990) “A state machine consists of state variables, which encode its state, and commands, which transform its state. Each command is implemented by a deterministic program; execution of the command modifies the state variables and/or produces some output. The defining characteristic of a state machine is that it specifies a deterministic computation that reads a stream of requests and processes each, occasionally producing output.”

De las operaciones CRUD nos quedaremos solo con Read y Update (solo podremos, después, leer y modificar un campo de un registro de una tabla de la base de datos).

Modelamos el estado de la SM con un objeto de una clase. El objeto tiene dos variables privadas reales (a y b) y métodos (comandos) públicos get(), set(valor), add(valor), mult(valor) para cada variable privada.

Dos métodos remotos: read(variable) and update(variable, operación: set, add, mult; valor). Los resultados para el update son true, false, y para el Read el valor de la variable.

Debe definirse, usando JSON por ejemplo, el formato de las solicitudes del cliente y de las respuestas del servidor.

La aplicación puede realizarse en cualquier lenguaje de programación.

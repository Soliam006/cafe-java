# Sesión 6 — ¡A jugar! La jornada y el bucle principal

> ⏱️ 3–4 horas · **Bucle principal · Estados · Menús · Descomposición en métodos**
>
> 📌 Necesitas todas las sesiones anteriores.

**Al acabar hoy tienes un juego. Uno de verdad, que se juega.** Es la sesión más larga y
la más satisfactoria.

---

## 1. Lo que vas a construir hoy

Tres clases que atan todo lo que has hecho hasta ahora:

- **La caja**: el dinero.
- **La jornada**: un día de trabajo, de la persiana arriba a la persiana abajo.
- **La partida**: la sucesión de días, el menú y el final.

---

## 2. La idea de hoy: quién manda sobre quién

Hasta ahora tus clases eran piezas sueltas. Hoy montas la torre, y hay una regla que
determina si va a ser mantenible o un plato de espaguetis:

> **Las flechas van hacia abajo, nunca hacia arriba.**

```
              Partida
                 │  crea y encadena
                 ▼
              Jornada
        ┌────────┼────────┬─────────┐
        ▼        ▼        ▼         ▼
      Caja    Almacén   Barra   Evaluador
                          │
                          ▼
                       Producto, Cliente, Pedido…
```

La partida conoce la jornada. La jornada conoce la caja, el almacén y la barra. Ninguno de
ellos sabe que la partida existe.

**¿Por qué importa tanto?** Porque si la caja necesitase preguntarle algo a la partida,
tendrías una dependencia circular: no podrías probar la caja sin montar una partida entera,
y cualquier cambio arriba rompería cosas abajo. Con las flechas hacia abajo, cada pieza se
puede probar sola. Que es justo lo que has estado haciendo cinco sesiones con tus
`PruebaN`.

---

## 3. Conceptos de Java

### 3.1 El bucle principal de un juego

Todos los videojuegos, desde el Pong hasta el último que hayas jugado, son este bucle:

```java
while (laPartidaSigue()) {
    mostrarSituacion();          // pintar el estado actual
    int accion = pedirAccion();  // leer al jugador
    aplicar(accion);             // cambiar el estado del mundo
}
mostrarFinal();
```

Se llama *game loop*. Tu jornada va a ser exactamente eso: mientras queden clientes y
quede género, atender al siguiente.

### 3.2 Menús con la vista

La entrada por teclado ya está resuelta. No escribas `Scanner`: usa esto.

```java
int opcion = Consola.menu("¿Qué haces?",
        "Atender al siguiente cliente",
        "Ver la carta",
        "Ver el almacén",
        "Cerrar la persiana");

switch (opcion) {
    case 1 -> atenderSiguiente();
    case 2 -> mostrarCarta();
    case 3 -> mostrarAlmacen();
    case 4 -> cerrar();
    default -> Consola.error("Eso no era una opción.");
}
```

Ese `switch` con flechas es la sintaxis moderna de Java: no necesita `break` y no se te
puede colar la ejecución al caso siguiente. Úsala.

Los demás métodos de lectura, todos validan solos y no te devuelven basura:

```java
int n         = Consola.leerEntero("¿Cuántos?", 1, 10);
String texto  = Consola.leerTexto("¿Cómo se llama tu cafetería?");
boolean si    = Consola.confirmar("¿Abres mañana?");
Consola.pausa();
```

### 3.3 Métodos cortos

Un `main` de 300 líneas es imposible de corregir, de depurar y de leer. La regla práctica:

> **Si un método no cabe en la pantalla, pártelo.**

Y hay una señal que casi nunca falla: si estás escribiendo un comentario del tipo
`// ahora cobramos al cliente`, eso que viene debajo es un método que se llama
`cobrarAlCliente()`. **Un buen nombre de método sustituye a un comentario.**

Los métodos privados de apoyo son gratis. Úsalos sin miedo.

---

## 4. Especificación: qué tienes que construir

Todo en `cafeteria.logica`.

### 4.1 La caja

**Qué representa:** el dinero del negocio.

**Qué guarda:** el saldo actual, el total ingresado y el total gastado (los dos últimos,
para las estadísticas del final).

**Qué sabe hacer:**

| Operación | Notas |
|---|---|
| Ingresar una cantidad | Rechaza cantidades negativas |
| Gastar una cantidad | Devuelve `false` (o lanza) si no hay saldo suficiente |
| Consultar saldo, total ingresado, total gastado | |
| ¿Está en números rojos? | Saldo ≤ 0 |

> ⚠️ **Nada de `setSaldo(double)`.** El saldo solo cambia por ingresar o gastar, y esos dos
> métodos validan. Si dejas un setter público, tarde o temprano alguien —tú, dentro de dos
> semanas— lo llamará desde algún sitio raro y tendrás dinero negativo sin saber de dónde
> sale. **Encapsular es proteger las reglas del negocio, no poner getters y setters a todo.**

### 4.2 Las estadísticas del día

**Qué representa:** el marcador de la jornada.

**Qué guarda:** clientes atendidos, cuántos perfectos, cuántos a medias, cuántos fallados,
cuántos se fueron por impaciencia, ingresos del día y coste del género gastado.

**Qué sabe hacer:** apuntar un resultado (que sume donde toque según la calidad) y devolver
sus contadores.

> 💡 Podría ser una clase pequeñita y tonta y estarías tentada de meter estos contadores
> sueltos dentro de la jornada. **No lo hagas.** Tenerlos juntos en su clase significa que
> mañana, cuando quieras estadísticas de toda la partida (sesión 7), sumas objetos de estos
> en vez de arrastrar siete variables por todas partes.

### 4.3 La jornada ⭐

**Qué representa:** un día de trabajo completo.

**Qué guarda:** el número de día, la cola de clientes, el almacén, la caja, la barra, el
evaluador, la carta y las estadísticas del día.

**Qué sabe hacer:**

> **Jugar el día entero.** Es el bucle principal.

El esqueleto, en palabras:

```
mostrar el cartel del día
mientras (queden clientes) y (no se haya acabado el género) y (la jugadora quiera seguir):

    mostrar el HUD
    sacar el siguiente cliente de la cola
    mostrarlo con su comanda

    para cada línea de su comanda:
        menú: qué producto de la carta preparo
        menú: qué opciones le pongo
        preparar en la barra
              ↳ si salta la excepción: avisar, no cobrar, seguir con el siguiente cliente
        evaluar contra lo que pedía esa línea
        mostrar el resultado
        ingresar en caja lo que toque
        apuntar en las estadísticas

    bajarle la paciencia a los que siguen esperando
    echar a los que se hayan hartado, y apuntarlo

mostrar el resumen del día
```

**Pártelo en métodos.** Ese esqueleto son cinco o seis métodos privados, no uno gigante:
atender al siguiente cliente, preparar una línea, pedir las opciones, actualizar la
paciencia de la cola, mostrar el resumen. Si al terminar tu método principal ocupa más de
30 líneas, todavía te queda partir.

**Qué más tiene que ofrecer hacia fuera:** un método para saber por qué acabó el día (no
quedaban clientes / se acabó el género / cerró la jugadora) y otro para dar sus
estadísticas. El `enum` para el motivo del final es buena idea.

**Los menús de opciones.** Cuando la jugadora prepara, le tienes que preguntar tamaño,
leche, extras… Ten cuidado de **no preguntar lo que no aplica**: si está preparando un
croissant no le preguntes el tipo de leche. Aquí es donde se nota si la sesión 2 la hiciste
bien: pregunta por las capacidades (`instanceof` con tus interfaces) y monta el menú según
lo que ese producto admita.

Para listar los valores de un `enum` en un menú:

```java
Tamanio[] tamanios = Tamanio.values();
String[] etiquetas = new String[tamanios.length];
for (int i = 0; i < tamanios.length; i++) etiquetas[i] = tamanios[i].getEtiqueta();
int elegido = Consola.menu("¿De qué tamaño?", etiquetas);
Tamanio tamanio = tamanios[elegido - 1];      // ojo: el menú devuelve desde 1
```

Ese `elegido - 1` es el clásico error de una unidad. Que no se te olvide.

### 4.4 La partida ⭐

**Qué representa:** la partida completa: varios días encadenados hasta que se acaba.

**Qué guarda:** el número de día actual, la caja (**esta sobrevive de un día a otro**), la
carta, el generador y las estadísticas acumuladas.

**Qué sabe hacer:**

> **Jugar.** El bucle de días.

```
pantalla de bienvenida
repetir:
    preparar el almacén del día   ← el día 1 sale gratis; a partir del 2, se paga
    crear la jornada y jugarla
    mostrar el resumen
    si la caja está en números rojos:
        fin de partida (mal final)
    preguntar: ¿abres mañana?
        no  → fin de partida (buen final)
        sí  → día++
mostrar la pantalla final
```

**La reposición del almacén** es lo que convierte esto en un juego de gestión de verdad:
al empezar cada día (menos el primero) hay que **pagar** el género. Calcula el coste con
los precios por unidad de la sesión 3, quítalo de la caja y, si no llega, se acabó la
partida. De repente cada café que tiras tiene un coste real.

Puedes hacerlo simple (reponer todo al máximo por un precio fijo) o con menú (elegir qué
repones y cuánto). Empieza por lo simple; el menú es una ampliación estupenda para después.

### 4.5 El punto de entrada

Una clase con el `main` en el paquete `cafeteria`. Que sea **corta**: crear la partida,
llamar a jugar y poco más. Un `main` largo es señal de que la lógica está en el sitio
equivocado.

---

## 5. Las llamadas a la vista que vas a necesitar

```java
Escena.bienvenida();
Escena.inicioDeDia(numeroDia, saldoCaja, clientesPrevistos);
Escena.hud(numeroDia, saldoCaja, clientesEnCola, porcentajeDeGenero);

Escena.clienteEnBarra(nombre, paciencia, pacienciaMaxima, frase);
Escena.ticketPedido(nombre, numeroPedido, lineasDeTexto);
Escena.preparando(nombreProducto, 800);
Escena.resultadoServicio(nombre, porcentaje, importe, comentarios);

Escena.carta(filasDeLaCarta);
Escena.inventario(filasDelAlmacen);

Escena.resumenDelDia(dia, atendidos, perfectos, aMedias, fallados, ingresos, gastos, saldo);
Escena.finDePartida(buenFinal, diasJugados, totalGanado, "el motivo, en una frase");
```

La lista completa, con todos los parámetros explicados, está en [API-VISTA.md](API-VISTA.md).

---

## 6. Buenas prácticas de hoy

**Las flechas hacia abajo.** Si te encuentras necesitando que una clase de abajo llame a
una de arriba, para y replantea. Casi siempre significa que ese trozo de lógica está en la
clase equivocada.

**Un método, una cosa.** `atenderCliente()` atiende a un cliente. No atiende y además
recalcula el stock y además pinta el resumen.

**Que la lógica no dependa del orden de los menús.** Si mañana cambias el orden de las
opciones y se rompe el juego, es que tenías reglas escondidas en los números del `switch`.

**Prueba jugando.** Es la primera sesión en la que puedes. Juega tres o cuatro partidas
enteras. Vas a encontrar cosas que ningún `PruebaN` te habría enseñado: que el día 3 es
imposible, que la paciencia baja demasiado rápido, que reponer sale carísimo. **Ajústalo
hasta que apetezca jugar otra vez.** Eso también es programar.

---

## 7. Errores típicos

| Lo que pasa | Por qué | Cómo se arregla |
|---|---|---|
| El bucle del día no acaba nunca | Se te olvidó sacar al cliente de la cola | `poll()`, no `peek()` |
| `NullPointerException` al empezar el día | Algo no lo inicializaste en el constructor | Repasa que todos los atributos se asignan |
| El menú elige el producto de al lado | El menú devuelve desde 1 y las listas van desde 0 | `elegido - 1` |
| El programa se cae al agotarse el género | Nadie captura la excepción de la barra | Captúrala en el método que prepara la línea |
| La caja se queda a 0 el día 2 | Reponer cuesta más de lo que ganas | Ajusta precios: es diseño de juego, no un bug |
| Te pregunta la leche para un croissant | El menú no mira qué admite el producto | Usa `instanceof` con tus interfaces |
| Las estadísticas no cuadran | Apuntas el resultado en dos sitios distintos | Un solo sitio donde se apunta |
| La partida no se acaba nunca | La condición de fin nunca se cumple | Escríbela aparte en un método `boolean` con nombre |

---

## 8. Cómo comprobar que funciona

**Juega.** Ese es el test de hoy. Pero juega con mala idea:

1. Una partida entera hasta que se acabe el género. ¿Sale el resumen correcto?
2. Otra fallando **todos** los pedidos a propósito. La caja tiene que hundirse y la partida
   acabar mal.
3. Otra acertándolos todos. ¿Puedes seguir abriendo días indefinidamente? ¿Cuántos aguantas?
4. Escribe cosas absurdas en los menús: letras, números enormes, negativos, intro a secas.
   **No se puede caer.** (`Consola` ya valida, pero comprueba que no te lo saltas en ningún
   sitio.)
5. Cierra el negocio el primer día. ¿Sale bien el final?
6. Deja que un cliente se harte de esperar. ¿Se va? ¿Se apunta en las estadísticas?

---

## ✅ Checklist de la sesión 6

- [ ] La caja no permite saldo negativo por accidente y no tiene setter.
- [ ] Las estadísticas del día están en su propia clase.
- [ ] La jornada tiene un bucle principal claro, partido en métodos cortos.
- [ ] El día acaba por las tres razones posibles y sé cuál ha sido.
- [ ] La partida encadena días y la caja se mantiene entre ellos.
- [ ] A partir del día 2 hay que pagar la reposición.
- [ ] Hay un final bueno y un final malo.
- [ ] Los menús no preguntan por opciones que ese producto no admite.
- [ ] Mi `main` ocupa menos de diez líneas.
- [ ] **He jugado una partida entera de principio a fin y me he divertido.**

---

## 🎯 Reto opcional

**Eventos aleatorios al empezar el día.** Un 20 % de las veces pasa algo:

- *"Hoy hace un calor horrible"* → todos los clientes piden bebidas frías.
- *"Se ha estropeado la cafetera"* → los cafés tardan el doble y la paciencia baja más rápido.
- *"Un grupo de turistas"* → cinco clientes extra, todos con prisa.
- *"Reparto retrasado"* → empiezas con la mitad del género.

Modélalos como un `enum` con un método que aplique el efecto, o como una interfaz `Evento`
con una clase por cada uno. **La segunda opción es mejor si vas a tener muchos y cada uno
hace cosas muy distintas; la primera si son cuatro y simples.** Elige y justifica.

---

**Siguiente:** [Sesión 7 — Estadísticas y pulido](07-estadisticas.md) 👉

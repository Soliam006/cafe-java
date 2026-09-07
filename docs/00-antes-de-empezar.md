# Sesión 0 — Antes de empezar

> ⏱️ 30–45 minutos · No escribes casi nada de código todavía. Hoy toca entender el terreno.

---

## 1. Las reglas del juego

Antes de programar nada hay que saber **qué** estamos programando. Estas son las reglas
completas de *Café Java*. Léelas enteras: todo lo que construyas durante las próximas
sesiones sale de aquí.

### El día

1. Empiezas la jornada con **dinero en caja** y un **almacén de ingredientes**.
2. Llegan clientes de uno en uno. Cada cliente trae una **comanda** con uno o varios productos.
3. Para cada línea de la comanda tú eliges de la carta: qué producto, de qué tamaño, con qué extras.
4. Preparar **siempre gasta ingredientes**, aciertes o no. Ese es el riesgo.
5. Se compara lo que has servido con lo que te habían pedido:

   | Resultado | Qué cobras | Cuándo pasa |
   |---|---|---|
   | 🟢 **Perfecto** | **100 %** del precio | El producto es el correcto y todos sus detalles coinciden |
   | 🟡 **A medias** | **50 %** del precio | Acertaste el producto pero fallaste algún detalle (tamaño, leche, un extra…) |
   | 🔴 **Mal** | **0 %** | Le has dado otro producto. Además pierdes los ingredientes que gastaste |

6. Cada cliente tiene **paciencia**. Si tardas demasiado en atenderle, se va sin pagar.

### El final del día

La jornada se acaba cuando pasa **una** de estas cosas:

- Ya no quedan clientes por atender.
- El almacén se ha quedado sin género y no puedes preparar nada.

Entonces sale el **resumen del día**: clientes atendidos, cuántos perfectos, cuántos a
medias, cuántos fallados, ingresos, coste del género gastado y saldo final. Y una pregunta:
**¿abres mañana o cierras el negocio?**

### El final de la partida

Si decides seguir, empieza un día nuevo: más clientes, más difícil, y tienes que **pagar
para reponer el almacén**. Si la caja no da para reponer, se acabó.

---

## 2. Cómo está montado el proyecto

Java organiza el código en **paquetes** (`package`), que son simplemente carpetas con un
nombre. Nuestro proyecto tiene tres:

```
src/
└── cafeteria/
    ├── vista/      ← YA ESTÁ HECHO. Todo lo que se ve por pantalla.
    ├── modelo/     ← TÚ. Las "cosas" del juego: productos, clientes, pedidos.
    └── logica/     ← TÚ. Las "reglas": inventario, evaluación, la partida.
```

### ¿Por qué tres carpetas y no una?

Porque **separar responsabilidades** es la idea de fondo de toda la asignatura. Fíjate:

- Una clase `Producto` sabe **cuánto cuesta un café con leche grande**. No sabe nada de
  colores ni de tablas.
- La clase `Escena` sabe **dibujar una tabla bonita**. No sabe nada de cafés.
- La clase que lleva la partida coge los datos del modelo y se los pasa a la vista.

Esa frontera se llama **separación entre modelo y vista**, y tiene una consecuencia muy
concreta que vas a ver hoy mismo: **ningún método de la vista recibe un objeto tuyo**.
Solo recibe textos y números. Abre `src/cafeteria/vista/Escena.java` y compruébalo.

Ventaja práctica: puedes llamar a tus clases como quieras y cambiarles el nombre veinte
veces, que la vista sigue funcionando igual.

---

## 3. Ejecuta la vista y mira lo que ya tienes

Abre `src/cafeteria/vista/DemoVista.java` y dale al botón verde (o `Ctrl+Shift+F10`).

Vas a ver, en orden: el logo, la pantalla de reglas, el cartel del día 1, la barra de
estado, la carta, la ficha de un producto, un cliente en la barra, su comanda, una
animación de preparación, los tres resultados posibles, el almacén, el cierre del día y
el fin de partida.

**Todo eso ya funciona.** Ninguna de esas pantallas la vas a tener que programar. Lo que
vas a programar es lo que las llena de datos.

> 💡 Deja `DemoVista` abierto en una pestaña durante todo el proyecto. Cuando necesites
> enseñar algo por pantalla, busca ahí la pantalla que más se parezca, mira cómo se llama
> y llámala tú con tus datos. La lista completa está en [API-VISTA.md](API-VISTA.md).

### La consola no me pinta bien

| Síntoma | Solución |
|---|---|
| Aparece `[32m`, `[0m`… por todas partes | Tu terminal no entiende colores. Escribe `Ansi.desactivar();` como primera línea de tu `main` |
| Los acentos salen como `Ã³` o `?` | En IntelliJ: *File → Settings → Editor → File Encodings* → poner **UTF-8** en las tres opciones |
| Los bordes de las cajas salen rotos | Cambia la fuente de la consola a una monoespaciada moderna: *Settings → Editor → Color Scheme → Console Font* → `JetBrains Mono` o `Consolas` |

---

## 4. Cómo vas a trabajar

Cada sesión tiene la misma estructura:

1. **Lo que vas a construir hoy** — el objetivo, en una frase.
2. **Conceptos de Java** — la teoría mínima, con ejemplos de *otro* tema para que veas la
   sintaxis sin que te den la solución masticada.
3. **Especificación de las clases** — el corazón. Qué representa cada clase, qué tiene que
   guardar y qué tiene que saber hacer. **En prosa, no en código.**
4. **Decisiones que tomas tú** — los nombres, sobre todo. Apúntalos en [NOMBRES.md](NOMBRES.md).
5. **Buenas prácticas de hoy** y **errores típicos**.
6. **Cómo comprobar que funciona** — un pequeño programa de prueba que escribes tú.
7. **Checklist** para saber que has terminado.
8. **Reto opcional**, si te has quedado con ganas.

### Sobre los nombres

Las guías **no te dan los nombres de tus clases ni de tus métodos**. Te dicen qué tiene
que hacer cada cosa y tú decides cómo se llama. Es a propósito: elegir buenos nombres es
la mitad de programar bien.

Lo único que te pido: **apúntalos en [NOMBRES.md](NOMBRES.md) según los decidas**. En la
sesión 5 vas a necesitar acordarte de cómo llamaste a algo de la sesión 1.

---

## 5. Convenciones de Java que damos por sabidas

Si algo de esto no te suena, míralo antes de seguir. Es lo que espera cualquier profesor
al corregir:

| Cosa | Convención | Ejemplo |
|---|---|---|
| Clases | `PascalCase`, sustantivo en singular | `Cliente`, `LineaDePedido` |
| Métodos | `camelCase`, empiezan por verbo | `calcularPrecio()`, `estaVacio()` |
| Variables | `camelCase`, descriptivas | `precioTotal`, `clientesAtendidos` |
| Constantes | `MAYUSCULAS_CON_GUION_BAJO` | `PRECIO_MAXIMO` |
| Paquetes | todo en minúsculas | `cafeteria.modelo` |
| Valores de un `enum` | `MAYUSCULAS` | `GRANDE`, `SIN_LECHE` |
| Un fichero | una clase pública, con el mismo nombre | `Cliente.java` → `class Cliente` |

Y dos reglas de oro que te van a acompañar todo el proyecto:

> **1. Los atributos van `private`. Siempre.**
> Si algo de fuera necesita leerlos, le pones un *getter*. Si necesita cambiarlos, te
> paras a pensar si de verdad hace falta un *setter*, porque muchas veces no.

> **2. Una clase, una responsabilidad.**
> Si al describir una clase tienes que usar la palabra "y" dos veces, probablemente
> deberían ser dos clases.

---

## 6. Guarda tu trabajo desde el primer día

Este proyecto vive en GitHub y **tú tienes tu propia rama**, llamada `desarrollo`. Ahí subes
lo que vas haciendo, aunque esté a medias y aunque no funcione todavía.

```bash
git switch desarrollo     # colócate en tu rama (compruébalo con git branch)
```

Y al acabar cada rato de trabajo, siempre lo mismo:

```bash
git add .
git commit -m "Sesión 1: enums de tamaño y tipo de leche"
git push
```

Cuando termines una sesión entera y esté revisada, se lleva de `desarrollo` a `main` con un
*pull request*.

**No lo dejes para el final.** Un proyecto sin historial es un proyecto que, el día que
rompas algo, no puedes recuperar. Y el historial de commits acaba siendo una de las cosas
más enseñables que vas a sacar de aquí.

👉 Todo explicado, con la chuleta de comandos y cómo hacerlo desde IntelliJ sin escribir
nada: **[GIT.md](GIT.md)**.

---

## ✅ Checklist de la sesión 0

- [ ] Has leído las reglas del juego y las entiendes.
- [ ] `DemoVista` se ejecuta y se ve bien (colores, bordes, acentos).
- [ ] Sabes en qué carpeta va tu código y en cuál no.
- [ ] Entiendes por qué la vista no conoce tus clases.
- [ ] Has abierto [NOMBRES.md](NOMBRES.md) y lo tienes a mano.
- [ ] Estás en la rama `desarrollo` y has leído [GIT.md](GIT.md).
- [ ] Has hecho tu primer `commit` y `push`, aunque sea de una tontería, para comprobar
      que tienes permisos y que funciona.

---

**Siguiente:** [Sesión 1 — La carta: productos](01-productos.md) 👉

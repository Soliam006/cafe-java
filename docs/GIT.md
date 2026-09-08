# Cómo trabajar con Git y GitHub en este proyecto

> Léelo una vez entero antes de empezar la sesión 1. Luego vuelve a la chuleta del final
> cada vez que acabes una sesión.

No hace falta que entiendas Git a fondo para usarlo. Con seis comandos vas sobrada, y son
los mismos seis que usa cualquiera que trabaje programando.

---

## 1. Git y GitHub no son lo mismo

- **Git** es un programa que vive en tu ordenador y guarda el historial de tu proyecto.
  Cada vez que haces un *commit*, guarda una foto de cómo estaba todo en ese momento. Puedes
  volver a cualquier foto anterior. **Es la máquina del tiempo de tu código.**
- **GitHub** es una web donde se guarda una copia de ese historial, para tenerlo a salvo y
  para que otras personas lo vean.

Git funciona sin GitHub. GitHub no sirve de nada sin Git.

---

## 2. Cómo está organizado este repositorio

Hay **dos ramas**. Una rama es una línea de trabajo paralela.

```
   main         ●───────────────────────────●──────────────>   la versión "buena"
                 \                         /
   desarrollo     ●──●──●──●──●──●──●──●──●                     donde tú trabajas
                     s1 s2 s3 s4 s5 s6 s7
```

| Rama | Para qué |
|---|---|
| **`main`** | La versión revisada. Aquí **no se trabaja directamente** |
| **`desarrollo`** | **Tu sitio.** Aquí subes lo que vas haciendo, aunque esté a medias |

**Tú trabajas siempre en `desarrollo`.** Cuando terminas una sesión y está revisada, lo que
hay en `desarrollo` se lleva a `main` con un *pull request* (lo vemos en el punto 6).

¿Por qué complicarse así? Porque es exactamente como se trabaja en cualquier empresa y en
cualquier proyecto serio: hay una rama estable que nunca se rompe y otra donde se
experimenta. Aprenderlo ahora te lo ahorra en primero de prácticas.

---

## 3. Preparar tu ordenador (solo una vez)

Instala Git desde [git-scm.com](https://git-scm.com) si no lo tienes, y dile quién eres.
Este nombre y este correo van a aparecer en cada commit que hagas:

```bash
git config --global user.name "Tu Nombre"
git config --global user.email "tucorreo@ejemplo.com"
```

Usa el mismo correo que tengas en GitHub para que te reconozca los commits como tuyos.

### Bájate el proyecto y colócate en tu rama

```bash
git clone https://github.com/Soliam006/cafe-java.git
cd cafe-java
git switch desarrollo
```

Ese `git switch desarrollo` es importante. Si te lo saltas estarás en `main`, y no quieres
trabajar ahí.

**Para comprobar en qué rama estás en cualquier momento:**

```bash
git branch
```

La que tiene el asterisco es la tuya. En IntelliJ también sale abajo a la derecha, en la
barra de estado.

---

## 4. El ciclo de todos los días

Son cuatro pasos y siempre los mismos.

### Antes de empezar a trabajar: bájate lo que haya nuevo

```bash
git pull
```

Si has trabajado solo tú, no traerá nada. Si alguien ha tocado algo, lo trae. **Cógelo por
costumbre**: es lo que evita el 90 % de los líos.

### Mientras trabajas: mira qué has tocado

```bash
git status

```

Te dice qué ficheros has cambiado, cuáles son nuevos y cuáles están listos para guardar.
Cuando algo no te cuadre, `git status` es siempre la primera pregunta.

### Al acabar un rato de trabajo: guarda una foto

```bash
git add .
git commit -m "Sesión 1: enums de tamaño y tipo de leche"
```

- `git add .` marca todos tus cambios para incluirlos.
- `git commit -m "..."` guarda la foto con ese mensaje.

### Sube la foto a GitHub

```bash
git push
```

Y ya está en la nube. Si se te muere el portátil, tu trabajo sigue ahí.

---

## 5. Cómo escribir mensajes de commit

El mensaje explica **qué has hecho**, no qué ficheros has tocado (eso ya lo sabe Git).

| ⛔ Malo | ✅ Bueno |
|---|---|
| `cambios` | `Sesión 1: clase abstracta de producto con sus dos métodos abstractos` |
| `asdf` | `Añadido el enum de tamaños con multiplicador de precio` |
| `arreglado` | `Corregido el cálculo de precio: no sumaba el recargo de la leche` |
| `Update Producto.java` | `El almacén ahora consume de forma atómica` |

La regla: alguien que lea solo la lista de mensajes debería entender qué ha pasado en el
proyecto. Y ese alguien vas a ser tú dentro de tres meses.

**Un consejo de ritmo:** haz commit cada vez que termines algo que funciona, aunque sea
pequeño. Cinco commits en una tarde es normal y sano. Uno gigante al final de la semana es
lo que hace la gente que luego no encuentra dónde se rompió algo.

---

## 6. Al terminar una sesión: el pull request

Un **pull request** (PR) es una propuesta: *"he hecho esto en mi rama, ¿lo llevamos a
`main`?"*. Sirve para que alguien lo revise antes.

1. Asegúrate de haber hecho `git push` con todo.
2. Entra en [el repositorio](https://github.com/Soliam006/cafe-java).
3. Te saldrá un aviso amarillo *"desarrollo had recent pushes — Compare & pull request"*.
   Dale.
   (Si no sale: pestaña **Pull requests → New pull request**, y elige
   `base: main` ← `compare: desarrollo`.)
4. Ponle un título tipo **"Sesión 3: el almacén"** y en la descripción cuenta:
   - qué has hecho,
   - qué decisiones de diseño has tomado y por qué,
   - qué te ha costado o de qué no estás segura.
5. **Create pull request**.

Ese punto 4 es el que de verdad vale la pena. Escribir lo que has hecho te obliga a
ordenarlo en la cabeza, y muchas veces encuentras un fallo mientras lo escribes.

Cuando esté revisado y aprobado, se hace **Merge** y tu trabajo pasa a `main`. Después,
vuelve a poner tu rama al día:

```bash
git switch desarrollo
git pull origin main
git push
```

---

## 7. Hacerlo todo desde IntelliJ (sin escribir comandos)

IntelliJ lleva Git dentro y probablemente te resulte más cómodo:

| Qué quieres | Dónde |
|---|---|
| Ver en qué rama estás / cambiar de rama | Abajo a la derecha, en la barra de estado |
| Ver qué has cambiado | Panel **Commit** (`Alt+0`) |
| Hacer commit | Panel **Commit** → marca los ficheros, escribe el mensaje → **Commit** |
| Commit y subir de una vez | El mismo botón, con la flechita → **Commit and Push** |
| Bajarte lo nuevo | `Ctrl+T` (*Update Project*) |
| Ver el historial | Panel **Git** (`Alt+9`) → pestaña **Log** |
| Deshacer un cambio en un fichero | Clic derecho en el fichero → **Git → Rollback** |

**Lo mejor de IntelliJ para esto** es el panel Log: ves todos los commits, y clicando en uno
te enseña exactamente qué línea cambió, en verde y rojo. Cuando algo deje de funcionar y no
sepas por qué, ahí está la respuesta.

---

## 8. Cuando algo va mal

### `rejected — fetch first` al hacer push

Alguien subió algo antes que tú. Se arregla bajándolo primero:

```bash
git pull
git push
```

### Un conflicto

Pasa cuando dos personas cambian **la misma línea** del mismo fichero. Git no sabe con cuál
quedarse y te lo marca así en el fichero:

```
<<<<<<< HEAD
double precio = 2.10;
=======
double precio = 2.30;
>>>>>>> main
```

Abre el fichero, **borra las tres líneas de marcas** (`<<<<<<<`, `=======`, `>>>>>>>`) y
deja el código como tiene que quedar. Luego:

```bash
git add .
git commit -m "Resuelto el conflicto en el precio del capuchino"
```

IntelliJ tiene una ventana de tres columnas para esto (*Merge Conflicts*) que lo hace mucho
más llevadero. Y no te agobies: **un conflicto no rompe nada**, solo hay que elegir.

### "He liado algo y quiero volver atrás"

Mientras hayas hecho commits, **nada está perdido nunca**. Un fichero concreto:

```bash
git restore src/cafeteria/modelo/Producto.java
```

Y si el lío es gordo, no te pelees: pregunta antes de escribir comandos que has visto en
internet. `git reset --hard` y `git push --force` son los dos que de verdad borran cosas.

---

## 9. Qué no se sube nunca

El fichero `.gitignore` ya se encarga de casi todo, pero que lo sepas:

- La carpeta `out/` y los ficheros `.class`: son código compilado, se regeneran solos.
- `.idea/workspace.xml`: guarda qué pestañas tienes abiertas. No le interesa a nadie.
- Contraseñas, claves o tokens. **Nunca. En ningún proyecto. Jamás.** Un repositorio público
  lo lee todo el mundo, y borrarlo después no sirve porque queda en el historial.

---

## 10. Chuleta

```bash
git status                       # ¿qué he tocado?
git branch                       # ¿en qué rama estoy?
git switch desarrollo            # ir a mi rama

git pull                         # bajar lo nuevo
git add .                        # marcar todos mis cambios
git commit -m "mensaje claro"    # guardar la foto
git push                         # subirla a GitHub

git log --oneline                # ver el historial resumido
git restore <fichero>            # deshacer cambios de un fichero
```

**El ciclo, en una línea:**

```bash
git pull  →  trabajas  →  git add .  →  git commit -m "..."  →  git push
```

---

## 11. Un objetivo para el final del proyecto

Cuando acabes las siete sesiones, entra en la pestaña **Insights → Commits** del repositorio
y mira tu gráfico. Vas a tener un historial de semanas de trabajo con mensajes que explican
cómo fue creciendo el proyecto.

Eso, tal cual, es lo que se enseña en una entrevista. Vale mucho más que poner "Java" en un
currículum.

---

**Vuelta al índice:** [README](../README.md)

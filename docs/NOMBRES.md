# Mi libreta de nombres

Las guías no te dan los nombres: los eliges tú. **Apúntalos aquí según los vayas
decidiendo.** En la sesión 5 vas a necesitar acordarte de cómo llamaste algo en la sesión 1,
y esta página te lo va a resolver en diez segundos en vez de en diez minutos.

Rellena solo lo que hayas hecho. Va a quedar hecho un cristo al principio y perfecto al
final: así son los proyectos.

---

## Sesión 1 — Productos

| Qué es                              | Cómo lo he llamado                                          |
|-------------------------------------|-------------------------------------------------------------|
| Enum de tamaños                     | Size                                                        |
| ↳ sus valores                       | Little, Medium, Large                                       |
| Enum de tipos de leche              | Milk                                                        |
| ↳ sus valores                       | Entera, Desnatada, Almendra, Semidesnatada, Coco, Soja      |
| Enum de comida                      | Eat                                                         |
| ↳ sus valores                       | Ensalada, Sandwich, Hamburgesa, Pizza, Pasta, Fruta, Yogurt |
| Enum de categorías                  | Category                                                    |
| ↳ sus valores                       | Cafe, Bebida fria, Reposteria, Salado, Snack, Otro          |
| **Clase abstracta de producto**     |                                                             |
| ↳ método abstracto: calcular precio | public abstract double calcularPrecioFinal();               |
| ↳ método abstracto: describirse     | - - -                                                       |
| Hija: bebida caliente               | HotDrink                                                    |
| Hija: bebida fría                   | IceDrink                                                    |
| Hija: repostería                    | Confectionery                                               |
| Hija: salado (opcional)             | Meal                                                        |

---

## Sesión 2 — Interfaces

| Qué es | Cómo lo he llamado |
|---|---|
| Interfaz "admite extras" | |
| ↳ sus métodos | |
| Interfaz "se puede calentar" | |
| ↳ sus métodos | |
| Interfaz opcional (promociones) | |
| Clase carta / catálogo | |
| ↳ método de fábrica de la carta | |
| ↳ ¿devuelvo `null` u `Optional` al buscar? | |

---

## Sesión 3 — Almacén

| Qué es | Cómo lo he llamado |
|---|---|
| Ingredientes (¿enum o clase?) | |
| ↳ sus valores / atributos | |
| Clase almacén | |
| ↳ método de consumir | |
| ↳ método de reponer | |
| ↳ método de porcentaje restante | |
| **Mi excepción** | |
| ↳ qué datos lleva dentro | |
| Método abstracto de ingredientes (en producto) | |

---

## Sesión 4 — Clientes

| Qué es | Cómo lo he llamado |
|---|---|
| Ficha de opciones / personalización | |
| ↳ sus campos | |
| Método abstracto "créate con estas opciones" | |
| Clase línea de comanda | |
| Clase pedido | |
| Clase cliente | |
| ↳ método de perder paciencia | |
| Generador de clientes | |
| **Mi regla de pérdida de paciencia** | |
| **Mi curva de dificultad** | |

---

## Sesión 5 — Evaluación

| Qué es | Cómo lo he llamado |
|---|---|
| Enum de calidad del servicio | |
| ↳ sus valores y porcentajes | |
| Clase resultado del servicio | |
| Clase barra / puesto de preparación | |
| Clase evaluador | |
| ↳ método de evaluar una línea | |
| ↳ método de evaluar un pedido | |
| **¿Los extras se comparan con orden o sin él? ¿Por qué?** | |

---

## Sesión 6 — La partida

| Qué es | Cómo lo he llamado |
|---|---|
| Clase caja | |
| Clase estadísticas del día | |
| Clase jornada | |
| ↳ método del bucle principal | |
| Enum del motivo de fin de día | |
| Clase partida | |
| Clase con el `main` | |
| **Coste de reponer el almacén** | |

---

## Sesión 7 — Estadísticas

| Qué es | Cómo lo he llamado |
|---|---|
| Registro de ventas | |
| Estadísticas de la partida | |
| Clase del récord | |
| Nombre del fichero de récord | |
| **Qué guardo como récord** | |

---

## Decisiones de diseño que he tomado

Apunta aquí, en una frase, las decisiones que te ha costado tomar y por qué. Cuando alguien
te pregunte *"¿y por qué lo hiciste así?"* —un profesor, un compañero, tú misma en marzo—
esto vale su peso en oro.

| Decisión | Por qué |
|---|---|
| | |
| | |
| | |
| | |
| | |

---

## Cosas que me han costado

Y cómo las resolví. Esta tabla es la que más te va a servir dentro de seis meses.

| Qué se me atragantó | Cómo lo saqué |
|---|---|
| | |
| | |
| | |

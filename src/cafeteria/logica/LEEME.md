# Paquete `logica` — las reglas del juego

Aquí van las clases que representan **qué pasa** en la cafeteria:

- La carta (catálogo de productos)
- El almacén y su excepción
- El generador de clientes
- La barra (donde se prepara) y el evaluador (quien juzga)
- La caja, las estadísticas, la jornada y la partida

Estas clases **sí** pueden llamar a la vista: son las que orquestan el juego y deciden qué
se enseña por pantalla.

Regla de dependencias: las flechas van hacia abajo. La partida conoce la jornada, la
jornada conoce la caja y el almacén, y ninguno de ellos sabe que la partida existe.

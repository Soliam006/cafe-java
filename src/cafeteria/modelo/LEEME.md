# Paquete `modelo` — las "cosas" del juego

Aquí van las clases que representan **qué existe** en la cafeteria:

- Los enums (tamaño, tipo de leche, categoría)
- La clase abstracta de producto y sus hijas
- Las interfaces de capacidades (admite extras, se puede calentar)
- La ficha de opciones, la línea de comanda, el pedido y el cliente

**Regla de este paquete: aquí no se imprime nada.** Ni un `System.out.println`, ni una
llamada a `Consola`. Estas clases devuelven datos y textos; quien las llama decide si los
pinta.

Empieza por la guía: `docs/01-productos.md`

# Geometría plana

[Índice principal](../../README.md) · [Catálogo de módulos](../README.md)

El módulo calcula área o perímetro para las figuras planas. Las medidas lineales
usan la unidad seleccionada y las áreas usan esa unidad al cuadrado.

## Operaciones

| Figura | Operaciones |
|---|---|
| Cuadrado | Área y perímetro |
| Rectángulo | Área y perímetro |
| Triángulo | Área base-altura, área de Herón y perímetro |
| Equilátero | Área y perímetro |
| Isósceles | Área y perímetro |
| Triángulo rectángulo | Área y perímetro |
| Paralelogramo | Área y perímetro |
| Rombo | Área y perímetro |
| Trapecio | Área y perímetro |
| Cometa | Área y perímetro |
| Círculo | Área y circunferencia |
| Semicírculo | Área y perímetro |
| Sector circular | Área y longitud de arco |
| Corona circular | Área y perímetro total |
| Polígono regular | Área y perímetro a partir del número de lados y longitud de lado |

La categoría contiene 31 calculadoras. Las desigualdades geométricas se validan
en dominio: lados positivos, desigualdad triangular y radio exterior mayor que
el interior. El sector recibe el ángulo seleccionado y convierte el valor para
su calculador.

## Código y pruebas

- Calculadoras: `domain/calculators/plane/` y `domain/calculators/geometry/`.
- Modelos: `domain/shapes/plane/`.
- Registro: `AppConfig.registerPlane()`.
- Adaptación de campos: bloque de geometría plana en `FormulaInputFactory`.
- Pruebas: `PlaneAreaTest`, `PlanePerimeterTest`, `PlaneValidationTest` y
  `CircleAreaCalculatorTest`.

Consulta [unidades y validación](../../componentes/dominio/README.md),
[cómo añadir operaciones](../../docs/como-anadir-formulas.md) o vuelve al
[índice principal](../../README.md).
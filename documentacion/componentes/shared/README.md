# Tipos compartidos

[Índice principal](../../README.md) · [Componentes técnicos](../README.md)

El paquete `com.mathedu.shared` agrupa conceptos usados por varias capas, sin
contener interfaz gráfica ni algoritmos concretos:

- `result/CalculationResult`: salida uniforme, pasos de resolución, unidad y
  marca de exactitud/aproximación.
- `errors/ErrorCode` y `errors/CalcException`: códigos y errores tipados, con
  mensaje dirigido al usuario y campo opcional.
- `validation/ValidationResult`: representación compartida de resultado de
  validación.
- `units/LengthUnit` y `units/AngleUnit`: unidades físicas y conversiones.

Las reglas específicas siguen en dominio; esta capa evita duplicar contratos
comunes. Consulta [dominio](../dominio/README.md),
[flujo de cálculo](../../docs/flujo-de-calculo.md) y el
[índice principal](../../README.md).
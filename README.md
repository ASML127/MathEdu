# MathEdu v1.0.0

**MathEdu** es una herramienta de asistencia y cálculo matemático desarrollada con el propósito de servir como recurso de apoyo académico para estudiantes desde la educación secundaria básica hasta la educación superior. Este proyecto busca facilitar el aprendizaje, la comprensión y la resolución de problemas en diversas áreas de las matemáticas, promoviendo el acceso a herramientas tecnológicas educativas de calidad.

> **Autor:** Ángel Stephane Montero Lastre  
> *Ingeniero Informático — Universidad de Camagüey*  
> 
> *Software creado con vocación educativa para su distribución y uso en instituciones de enseñanza.*

---

## Módulos y Áreas de Estudio

MathEdu integra **121 fórmulas y operaciones** organizadas en módulos interactivos:

- **Geometría Plana y Perímetros/Áreas:** Cálculo de figuras geométricas fundamentales.
- **Aritmética y Álgebra:** Operaciones básicas, fracciones, porcentajes, productos notables, ecuaciones lineales y cuadráticas, y sistemas de ecuaciones (2×2 y 3×3).
- **Trigonometría:** Razones trigonométricas, identidades, resolución de triángulos (SSS, SAS, ASA) y conversión de unidades angulares.
- **Geometría Analítica:** Distancia, pendiente, rectas y circunferencias.
- **Geometría 3D (Sólidos):** Áreas laterales, totales y volúmenes de cuerpos geométricos.
- **Estadística y Probabilidad:** Medidas de tendencia central y dispersión, combinatoria, distribuciones binomial, Poisson y normal (vía Apache Commons Math).
- **Álgebra Lineal:** Vectores, operaciones matriciales, determinantes, inversas y sistemas de ecuaciones por eliminación de Gauss con pivoteo parcial.
- **Cálculo Diferencial e Integral:** Derivadas simbólicas, integrales indefinidas y definidas (con motor simbólico Symja y respaldo numérico de cuadratura).
- **Series y Sucesiones:** Progresiones aritméticas y geométricas, series infinitas, sumas finitas, criterios de convergencia y límites con aproximación bilateral.

---

## Requisitos del Sistema

- **Java Development Kit (JDK) 25** (o compatible con ejecución en bytecode Java 21). Asegúrate de que `JAVA_HOME` apunte correctamente a la instalación del JDK.
- **Apache Maven 3.9+** configurado en el `PATH`.

---

## Ejecución

### 1. Ejecución interactiva (con interfaz gráfica)
```bash
mvn javafx:run
```

### 2. Empaquetado y ejecución mediante JAR sombreado
Para generar el artefacto ejecutable con todas las dependencias incluidas:
```bash
mvn package
java -jar target/mathedu-1.0.0-shaded.jar
```

*(Nota: La aplicación cuenta con un lanzador optimizado que evita restricciones del runtime de JavaFX en entornos de escritorio).*

---

## Documentación Técnica

Si estás interesado en la arquitectura del sistema, la Guía del Desarrollador, el diseño de capas o la estrategia de pruebas automatizadas, consulta la [documentación completa del proyecto](documentacion/README.md).

package com.mathedu.application.calculation;

import com.mathedu.domain.calculators.algebra.AbsCalculator;
import com.mathedu.domain.calculators.algebra.AddCalculator;
import com.mathedu.domain.calculators.algebra.BinomialDiffSquareCalculator;
import com.mathedu.domain.calculators.algebra.BinomialSquareCalculator;
import com.mathedu.domain.calculators.algebra.DivideCalculator;
import com.mathedu.domain.calculators.algebra.ExpCalculator;
import com.mathedu.domain.calculators.algebra.ExpressionCalculator;
import com.mathedu.domain.calculators.algebra.FractionsCalculator;
import com.mathedu.domain.calculators.algebra.InequalityCalculator;
import com.mathedu.domain.calculators.algebra.LinearEquationCalculator;
import com.mathedu.domain.calculators.algebra.LogCalculator;
import com.mathedu.domain.calculators.algebra.MultiplyCalculator;
import com.mathedu.domain.calculators.algebra.PercentCalculator;
import com.mathedu.domain.calculators.algebra.PercentOfCalculator;
import com.mathedu.domain.calculators.algebra.PolyAddCalculator;
import com.mathedu.domain.calculators.algebra.PolyDivideCalculator;
import com.mathedu.domain.calculators.algebra.PolyEvaluateCalculator;
import com.mathedu.domain.calculators.algebra.PolyFactorCalculator;
import com.mathedu.domain.calculators.algebra.PolyMultiplyCalculator;
import com.mathedu.domain.calculators.algebra.PolySubtractCalculator;
import com.mathedu.domain.calculators.algebra.PowerCalculator;
import com.mathedu.domain.calculators.algebra.QuadraticEquationCalculator;
import com.mathedu.domain.calculators.algebra.RootCalculator;
import com.mathedu.domain.calculators.algebra.RuleOfThreeCalculator;
import com.mathedu.domain.calculators.algebra.SubtractCalculator;
import com.mathedu.domain.calculators.algebra.SumDifferenceCalculator;
import com.mathedu.domain.calculators.algebra.System2x2Calculator;
import com.mathedu.domain.calculators.algebra.System3x3Calculator;
import com.mathedu.domain.calculators.analytic.AnalyticGeometryCalculator;
import com.mathedu.domain.calculators.solid.SolidMetricsCalculator;
import com.mathedu.domain.calculators.stats.StatisticsCalculator;
import com.mathedu.domain.calculators.linear.LinearAlgebraCalculator;
import com.mathedu.domain.calculators.calculus.CalculusCalculator;
import com.mathedu.domain.calculators.series.SeriesCalculator;
import com.mathedu.domain.calculators.geometry.CircleAreaCalculator;
import com.mathedu.domain.calculators.geometry.CircleAreaInput;import com.mathedu.domain.calculators.plane.AnnulusAreaCalculator;
import com.mathedu.domain.calculators.plane.AnnulusPerimeterCalculator;
import com.mathedu.domain.calculators.plane.CirclePerimeterCalculator;
import com.mathedu.domain.calculators.plane.EquilateralAreaCalculator;
import com.mathedu.domain.calculators.plane.EquilateralPerimeterCalculator;
import com.mathedu.domain.calculators.plane.IsoscelesAreaCalculator;
import com.mathedu.domain.calculators.plane.IsoscelesPerimeterCalculator;
import com.mathedu.domain.calculators.plane.KiteAreaCalculator;
import com.mathedu.domain.calculators.plane.KitePerimeterCalculator;
import com.mathedu.domain.calculators.plane.ParallelogramAreaCalculator;
import com.mathedu.domain.calculators.plane.ParallelogramPerimeterCalculator;
import com.mathedu.domain.calculators.plane.RectangleAreaCalculator;
import com.mathedu.domain.calculators.plane.RectanglePerimeterCalculator;
import com.mathedu.domain.calculators.plane.RegularPolygonAreaCalculator;
import com.mathedu.domain.calculators.plane.RegularPolygonPerimeterCalculator;
import com.mathedu.domain.calculators.plane.RhombusAreaCalculator;
import com.mathedu.domain.calculators.plane.RhombusPerimeterCalculator;
import com.mathedu.domain.calculators.plane.RightAreaCalculator;
import com.mathedu.domain.calculators.plane.RightPerimeterCalculator;
import com.mathedu.domain.calculators.plane.SectorArcCalculator;
import com.mathedu.domain.calculators.plane.SectorAreaCalculator;
import com.mathedu.domain.calculators.plane.SemicircleAreaCalculator;
import com.mathedu.domain.calculators.plane.SemicirclePerimeterCalculator;
import com.mathedu.domain.calculators.plane.SquareAreaCalculator;
import com.mathedu.domain.calculators.plane.SquarePerimeterCalculator;
import com.mathedu.domain.calculators.plane.TrapezoidAreaCalculator;
import com.mathedu.domain.calculators.plane.TrapezoidPerimeterCalculator;
import com.mathedu.domain.calculators.plane.TriangleBaseHeightCalculator;
import com.mathedu.domain.calculators.plane.TriangleHeronCalculator;
import com.mathedu.domain.calculators.plane.TrianglePerimeterCalculator;
import com.mathedu.domain.calculators.trig.AcosCalculator;
import com.mathedu.domain.calculators.trig.AngleConvertCalculator;
import com.mathedu.domain.calculators.trig.AsinCalculator;
import com.mathedu.domain.calculators.trig.AtanCalculator;
import com.mathedu.domain.calculators.trig.CosCalculator;
import com.mathedu.domain.calculators.trig.IdentitiesCalculator;
import com.mathedu.domain.calculators.trig.PythagorasHypCalculator;
import com.mathedu.domain.calculators.trig.PythagorasLegCalculator;
import com.mathedu.domain.calculators.trig.SinCalculator;
import com.mathedu.domain.calculators.trig.SolveAsaCalculator;
import com.mathedu.domain.calculators.trig.SolveSasCalculator;
import com.mathedu.domain.calculators.trig.SolveSssCalculator;
import com.mathedu.domain.calculators.trig.TanCalculator;
import com.mathedu.domain.validation.InputParser;
import com.mathedu.shared.errors.CalcException;
import com.mathedu.shared.errors.ErrorCode;
import com.mathedu.shared.units.AngleUnit;
import com.mathedu.shared.units.LengthUnit;

import java.util.Map;

/**
 * Construye el input tipado de cada formula a partir del texto crudo
 * del formulario dinamico. Centraliza el parseo (coma decimal, enteros,
 * conversion de angulos) para que el ViewModel no conozca 30 records.
 *
 * <p>Las validaciones de rango viven en cada {@code Calculator} via
 * {@code Guards}; aqui solo se parsea.
 */
public final class FormulaInputFactory {

    public Object buildInput(String formulaId, Map<String, String> raw,
                             LengthUnit lengthUnit, AngleUnit angleUnit) throws CalcException {
        LengthUnit unit = lengthUnit == null ? LengthUnit.CM : lengthUnit;
        AngleUnit au = angleUnit == null ? AngleUnit.DEG : angleUnit;
                SolidMetricsCalculator.Kind solidKind = SolidMetricsCalculator.findKind(formulaId);
                if (solidKind != null) {
                        return solidInput(raw, unit, solidKind);
                }
        return switch (formulaId) {
            case null -> throw new CalcException(ErrorCode.UNKNOWN_FORMULA,
                    "formulaId", "Ninguna formula seleccionada.");
            case CircleAreaCalculator.FORMULA_ID ->
                    new CircleAreaInput(num(raw, "r", "Radio"), unit);
            case CirclePerimeterCalculator.FORMULA_ID ->
                    new CirclePerimeterCalculator.Input(num(raw, "r", "Radio"), unit);
            case SquareAreaCalculator.FORMULA_ID ->
                    new SquareAreaCalculator.Input(num(raw, "side", "Lado"), unit);
            case SquarePerimeterCalculator.FORMULA_ID ->
                    new SquarePerimeterCalculator.Input(num(raw, "side", "Lado"), unit);
            case RectangleAreaCalculator.FORMULA_ID ->
                    new RectangleAreaCalculator.Input(
                            num(raw, "width", "Base"), num(raw, "height", "Altura"), unit);
            case RectanglePerimeterCalculator.FORMULA_ID ->
                    new RectanglePerimeterCalculator.Input(
                            num(raw, "width", "Base"), num(raw, "height", "Altura"), unit);
            case TriangleBaseHeightCalculator.FORMULA_ID ->
                    new TriangleBaseHeightCalculator.Input(
                            num(raw, "base", "Base"), num(raw, "height", "Altura"), unit);
            case TriangleHeronCalculator.FORMULA_ID ->
                    new TriangleHeronCalculator.Input(
                            num(raw, "a", "Lado a"), num(raw, "b", "Lado b"),
                            num(raw, "c", "Lado c"), unit);
            case TrianglePerimeterCalculator.FORMULA_ID ->
                    new TrianglePerimeterCalculator.Input(
                            num(raw, "a", "Lado a"), num(raw, "b", "Lado b"),
                            num(raw, "c", "Lado c"), unit);
            case EquilateralAreaCalculator.FORMULA_ID ->
                    new EquilateralAreaCalculator.Input(num(raw, "side", "Lado"), unit);
            case EquilateralPerimeterCalculator.FORMULA_ID ->
                    new EquilateralPerimeterCalculator.Input(num(raw, "side", "Lado"), unit);
            case IsoscelesAreaCalculator.FORMULA_ID ->
                    new IsoscelesAreaCalculator.Input(
                            num(raw, "base", "Base"), num(raw, "height", "Altura"), unit);
            case IsoscelesPerimeterCalculator.FORMULA_ID ->
                    new IsoscelesPerimeterCalculator.Input(
                            num(raw, "base", "Base"), num(raw, "equalSide", "Lado igual"), unit);
            case RightAreaCalculator.FORMULA_ID ->
                    new RightAreaCalculator.Input(
                            num(raw, "legA", "Cateto a"), num(raw, "legB", "Cateto b"), unit);
            case RightPerimeterCalculator.FORMULA_ID ->
                    new RightPerimeterCalculator.Input(
                            num(raw, "legA", "Cateto a"), num(raw, "legB", "Cateto b"), unit);
            case ParallelogramAreaCalculator.FORMULA_ID ->
                    new ParallelogramAreaCalculator.Input(
                            num(raw, "base", "Base"), num(raw, "height", "Altura"), unit);
            case ParallelogramPerimeterCalculator.FORMULA_ID ->
                    new ParallelogramPerimeterCalculator.Input(
                            num(raw, "base", "Base"), num(raw, "side", "Lado"),
                            num(raw, "height", "Altura"), unit);
            case RhombusAreaCalculator.FORMULA_ID ->
                    new RhombusAreaCalculator.Input(
                            num(raw, "diagMajor", "Diagonal mayor"),
                            num(raw, "diagMinor", "Diagonal menor"), unit);
            case RhombusPerimeterCalculator.FORMULA_ID ->
                    new RhombusPerimeterCalculator.Input(num(raw, "side", "Lado"), unit);
            case TrapezoidAreaCalculator.FORMULA_ID ->
                    new TrapezoidAreaCalculator.Input(
                            num(raw, "bigBase", "Base mayor"), num(raw, "smallBase", "Base menor"),
                            num(raw, "height", "Altura"), unit);
            case TrapezoidPerimeterCalculator.FORMULA_ID ->
                    new TrapezoidPerimeterCalculator.Input(
                            num(raw, "bigBase", "Base mayor"), num(raw, "smallBase", "Base menor"),
                            num(raw, "sideC", "Lado c"), num(raw, "sideD", "Lado d"), unit);
            case KiteAreaCalculator.FORMULA_ID ->
                    new KiteAreaCalculator.Input(
                            num(raw, "diagMajor", "Diagonal mayor"),
                            num(raw, "diagMinor", "Diagonal menor"), unit);
            case KitePerimeterCalculator.FORMULA_ID ->
                    new KitePerimeterCalculator.Input(
                            num(raw, "sideA", "Lado a"), num(raw, "sideB", "Lado b"), unit);
            case SemicircleAreaCalculator.FORMULA_ID ->
                    new SemicircleAreaCalculator.Input(num(raw, "r", "Radio"), unit);
            case SemicirclePerimeterCalculator.FORMULA_ID ->
                    new SemicirclePerimeterCalculator.Input(num(raw, "r", "Radio"), unit);
            case SectorAreaCalculator.FORMULA_ID ->
                    new SectorAreaCalculator.Input(
                            num(raw, "r", "Radio"), angleDeg(raw, au), unit);
            case SectorArcCalculator.FORMULA_ID ->
                    new SectorArcCalculator.Input(
                            num(raw, "r", "Radio"), angleDeg(raw, au), unit);
            case AnnulusAreaCalculator.FORMULA_ID ->
                    new AnnulusAreaCalculator.Input(
                            num(raw, "R", "Radio exterior"), num(raw, "r", "Radio interior"), unit);
            case AnnulusPerimeterCalculator.FORMULA_ID ->
                    new AnnulusPerimeterCalculator.Input(
                            num(raw, "R", "Radio exterior"), num(raw, "r", "Radio interior"), unit);
            case RegularPolygonAreaCalculator.FORMULA_ID ->
                    new RegularPolygonAreaCalculator.Input(
                            intNum(raw, "n", "N.º de lados"), num(raw, "side", "Lado"), unit);
            case RegularPolygonPerimeterCalculator.FORMULA_ID ->
                    new RegularPolygonPerimeterCalculator.Input(
                            intNum(raw, "n", "N.º de lados"), num(raw, "side", "Lado"), unit);
            // ---- Fase 2: aritmetica y algebra (sin unidades de longitud) ----
            case AddCalculator.FORMULA_ID ->
                    new AddCalculator.Input(num(raw, "a", "Sumando a"), num(raw, "b", "Sumando b"));
            case SubtractCalculator.FORMULA_ID ->
                    new SubtractCalculator.Input(
                            num(raw, "a", "Minuendo"), num(raw, "b", "Sustraendo"));
            case MultiplyCalculator.FORMULA_ID ->
                    new MultiplyCalculator.Input(
                            num(raw, "a", "Factor a"), num(raw, "b", "Factor b"));
            case DivideCalculator.FORMULA_ID ->
                    new DivideCalculator.Input(
                            num(raw, "a", "Dividendo"), num(raw, "b", "Divisor"));
            case ExpressionCalculator.FORMULA_ID ->
                    new ExpressionCalculator.Input(
                            text(raw, "expr", "Expresion"),
                            numOrDefault(raw, "x", "x", 0));
            case PercentCalculator.FORMULA_ID ->
                    new PercentCalculator.Input(
                            num(raw, "p", "Porcentaje"), num(raw, "base", "Cantidad"));
            case PercentOfCalculator.FORMULA_ID ->
                    new PercentOfCalculator.Input(
                            num(raw, "part", "Parte"), num(raw, "total", "Total"));
            case RuleOfThreeCalculator.FORMULA_ID ->
                    new RuleOfThreeCalculator.Input(
                            num(raw, "a", "Referencia A"), num(raw, "b", "Valor B"),
                            num(raw, "c", "Dato C"));
            case FractionsCalculator.FORMULA_ID ->
                    new FractionsCalculator.Input(
                            longNum(raw, "num1", "Numerador 1"), longNum(raw, "den1", "Denominador 1"),
                            text(raw, "op", "Operador"),
                            longNum(raw, "num2", "Numerador 2"), longNum(raw, "den2", "Denominador 2"));
            case PowerCalculator.FORMULA_ID ->
                    new PowerCalculator.Input(
                            num(raw, "base", "Base"), num(raw, "exp", "Exponente"));
            case RootCalculator.FORMULA_ID ->
                    new RootCalculator.Input(
                            num(raw, "x", "Radicando"), intNum(raw, "n", "Indice"));
            case LogCalculator.FORMULA_ID ->
                    new LogCalculator.Input(
                            num(raw, "x", "Argumento"), num(raw, "base", "Base"));
            case ExpCalculator.FORMULA_ID ->
                    new ExpCalculator.Input(num(raw, "x", "Exponente"));
            case AbsCalculator.FORMULA_ID ->
                    new AbsCalculator.Input(num(raw, "x", "Numero"));
            case LinearEquationCalculator.FORMULA_ID ->
                    new LinearEquationCalculator.Input(
                            num(raw, "a", "Coeficiente a"), num(raw, "b", "Termino b"),
                            num(raw, "c", "Segundo miembro c"));
            case QuadraticEquationCalculator.FORMULA_ID ->
                    new QuadraticEquationCalculator.Input(
                            num(raw, "a", "Coeficiente a"), num(raw, "b", "Coeficiente b"),
                            num(raw, "c", "Termino c"));
            case System2x2Calculator.FORMULA_ID ->
                    new System2x2Calculator.Input(
                            num(raw, "a1", "a1"), num(raw, "b1", "b1"), num(raw, "c1", "c1"),
                            num(raw, "a2", "a2"), num(raw, "b2", "b2"), num(raw, "c2", "c2"));
            case System3x3Calculator.FORMULA_ID ->
                    new System3x3Calculator.Input(
                            num(raw, "a1", "a1"), num(raw, "b1", "b1"),
                            num(raw, "c1", "c1"), num(raw, "d1", "d1"),
                            num(raw, "a2", "a2"), num(raw, "b2", "b2"),
                            num(raw, "c2", "c2"), num(raw, "d2", "d2"),
                            num(raw, "a3", "a3"), num(raw, "b3", "b3"),
                            num(raw, "c3", "c3"), num(raw, "d3", "d3"));
            case InequalityCalculator.FORMULA_ID ->
                    new InequalityCalculator.Input(
                            num(raw, "a", "Coeficiente a"), num(raw, "b", "Termino b"),
                            text(raw, "rel", "Relacion"));
            case PolyEvaluateCalculator.FORMULA_ID ->
                    new PolyEvaluateCalculator.Input(
                            text(raw, "coeffs", "Coeficientes"), num(raw, "x", "Valor de x"));
            case PolyFactorCalculator.FORMULA_ID ->
                    new PolyFactorCalculator.Input(text(raw, "coeffs", "Coeficientes"));
            case PolyAddCalculator.FORMULA_ID ->
                    new PolyAddCalculator.Input(
                            text(raw, "coeffA", "Coeficientes de P"),
                            text(raw, "coeffB", "Coeficientes de Q"));
            case PolySubtractCalculator.FORMULA_ID ->
                    new PolySubtractCalculator.Input(
                            text(raw, "coeffA", "Coeficientes de P"),
                            text(raw, "coeffB", "Coeficientes de Q"));
            case PolyMultiplyCalculator.FORMULA_ID ->
                    new PolyMultiplyCalculator.Input(
                            text(raw, "coeffA", "Coeficientes de P"),
                            text(raw, "coeffB", "Coeficientes de Q"));
            case PolyDivideCalculator.FORMULA_ID ->
                    new PolyDivideCalculator.Input(
                            text(raw, "coeffA", "Dividendo P"),
                            text(raw, "coeffB", "Divisor Q"));
            case BinomialSquareCalculator.FORMULA_ID ->
                    new BinomialSquareCalculator.Input(
                            num(raw, "a", "Termino a"), num(raw, "b", "Termino b"));
            case BinomialDiffSquareCalculator.FORMULA_ID ->
                    new BinomialDiffSquareCalculator.Input(
                            num(raw, "a", "Termino a"), num(raw, "b", "Termino b"));
            case SumDifferenceCalculator.FORMULA_ID ->
                    new SumDifferenceCalculator.Input(
                            num(raw, "a", "Termino a"), num(raw, "b", "Termino b"));
            // ---- Fase 2: trigonometria ----
            case SinCalculator.FORMULA_ID ->
                    new SinCalculator.Input(angleDeg(raw, au));
            case CosCalculator.FORMULA_ID ->
                    new CosCalculator.Input(angleDeg(raw, au));
            case TanCalculator.FORMULA_ID ->
                    new TanCalculator.Input(angleDeg(raw, au));
            case AsinCalculator.FORMULA_ID ->
                    new AsinCalculator.Input(num(raw, "x", "Valor"), au);
            case AcosCalculator.FORMULA_ID ->
                    new AcosCalculator.Input(num(raw, "x", "Valor"), au);
            case AtanCalculator.FORMULA_ID ->
                    new AtanCalculator.Input(num(raw, "x", "Valor"), au);
            case AngleConvertCalculator.FORMULA_ID ->
                    new AngleConvertCalculator.Input(num(raw, "value", "Valor"), au);
            case IdentitiesCalculator.FORMULA_ID ->
                    new IdentitiesCalculator.Input(angleDeg(raw, au));
            case PythagorasHypCalculator.FORMULA_ID ->
                    new PythagorasHypCalculator.Input(
                            num(raw, "legA", "Cateto a"), num(raw, "legB", "Cateto b"), unit);
            case PythagorasLegCalculator.FORMULA_ID ->
                    new PythagorasLegCalculator.Input(
                            num(raw, "hyp", "Hipotenusa"), num(raw, "legA", "Cateto conocido"),
                            unit);
            case SolveSssCalculator.FORMULA_ID ->
                    new SolveSssCalculator.Input(
                            num(raw, "a", "Lado a"), num(raw, "b", "Lado b"),
                            num(raw, "c", "Lado c"), unit, au);
            case SolveSasCalculator.FORMULA_ID ->
                    new SolveSasCalculator.Input(
                            num(raw, "a", "Lado a"), num(raw, "b", "Lado b"),
                            angleDegAs(raw, "angleC", "Angulo C", au), unit, au);
            case SolveAsaCalculator.FORMULA_ID ->
                    new SolveAsaCalculator.Input(
                            angleDegAs(raw, "angleA", "Angulo A", au),
                            angleDegAs(raw, "angleB", "Angulo B", au),
                            num(raw, "c", "Lado c"), unit, au);
            case AnalyticGeometryCalculator.Distance.FORMULA_ID ->
                    new AnalyticGeometryCalculator.Distance.Input(num(raw, "x1", "x1"),
                            num(raw, "y1", "y1"), num(raw, "x2", "x2"), num(raw, "y2", "y2"));
            case AnalyticGeometryCalculator.Midpoint.FORMULA_ID ->
                    new AnalyticGeometryCalculator.Midpoint.Input(num(raw, "x1", "x1"),
                            num(raw, "y1", "y1"), num(raw, "x2", "x2"), num(raw, "y2", "y2"));
            case AnalyticGeometryCalculator.Slope.FORMULA_ID ->
                    new AnalyticGeometryCalculator.Slope.Input(num(raw, "x1", "x1"),
                            num(raw, "y1", "y1"), num(raw, "x2", "x2"), num(raw, "y2", "y2"));
            case AnalyticGeometryCalculator.LineFromPoints.FORMULA_ID ->
                    new AnalyticGeometryCalculator.LineFromPoints.Input(num(raw, "x1", "x1"),
                            num(raw, "y1", "y1"), num(raw, "x2", "x2"), num(raw, "y2", "y2"));
            case AnalyticGeometryCalculator.PointSlope.FORMULA_ID ->
                    new AnalyticGeometryCalculator.PointSlope.Input(num(raw, "x1", "x1"),
                            num(raw, "y1", "y1"), num(raw, "m", "Pendiente"));
            case AnalyticGeometryCalculator.LineFromSlopeIntercept.FORMULA_ID ->
                    new AnalyticGeometryCalculator.LineFromSlopeIntercept.Input(
                            num(raw, "m", "Pendiente"), num(raw, "b", "Ordenada"));
            case AnalyticGeometryCalculator.GeneralLineConversion.FORMULA_ID ->
                    new AnalyticGeometryCalculator.GeneralLineConversion.Input(
                            num(raw, "a", "A"), num(raw, "b", "B"), num(raw, "c", "C"));
            case AnalyticGeometryCalculator.LineIntersection.FORMULA_ID ->
                    new AnalyticGeometryCalculator.LineIntersection.Input(
                            num(raw, "a1", "A1"), num(raw, "b1", "B1"), num(raw, "c1", "C1"),
                            num(raw, "a2", "A2"), num(raw, "b2", "B2"), num(raw, "c2", "C2"));
            case AnalyticGeometryCalculator.PointLineDistance.FORMULA_ID ->
                    new AnalyticGeometryCalculator.PointLineDistance.Input(
                            num(raw, "x", "x0"), num(raw, "y", "y0"),
                            num(raw, "a", "A"), num(raw, "b", "B"), num(raw, "c", "C"));
            case AnalyticGeometryCalculator.CircleEquation.FORMULA_ID ->
                    new AnalyticGeometryCalculator.CircleEquation.Input(
                            num(raw, "h", "Centro x"), num(raw, "k", "Centro y"),
                            num(raw, "r", "Radio"));
            case StatisticsCalculator.Descriptive.FORMULA_ID ->
                    new StatisticsCalculator.Descriptive.Input(
                            text(raw, "data", "Datos"), numOrDefault(raw, "percentile", "Percentil", 50));
            case StatisticsCalculator.Combination.FORMULA_ID ->
                    new StatisticsCalculator.Combination.Input(
                            intNum(raw, "n", "Elementos n"), intNum(raw, "r", "Seleccionados r"));
            case StatisticsCalculator.Permutation.FORMULA_ID ->
                    new StatisticsCalculator.Permutation.Input(
                            intNum(raw, "n", "Elementos n"), intNum(raw, "r", "Seleccionados r"));
            case StatisticsCalculator.BasicProbability.FORMULA_ID ->
                    new StatisticsCalculator.BasicProbability.Input(
                            num(raw, "favorable", "Casos favorables"), num(raw, "total", "Casos posibles"));
            case StatisticsCalculator.ConditionalProbability.FORMULA_ID ->
                    new StatisticsCalculator.ConditionalProbability.Input(
                            num(raw, "joint", "P(A y B)"), num(raw, "condition", "P(B)"));
            case StatisticsCalculator.Binomial.FORMULA_ID ->
                    new StatisticsCalculator.Binomial.Input(
                            intNum(raw, "n", "Ensayos n"), intNum(raw, "k", "Éxitos k"),
                            num(raw, "p", "Probabilidad p"));
            case StatisticsCalculator.Normal.FORMULA_ID ->
                    new StatisticsCalculator.Normal.Input(num(raw, "mean", "Media μ"),
                            num(raw, "sd", "Desviación estándar σ"), num(raw, "x", "Valor x"));
            case StatisticsCalculator.Poisson.FORMULA_ID ->
                    new StatisticsCalculator.Poisson.Input(num(raw, "mean", "Media λ"),
                            intNum(raw, "k", "Ocurrencias k"));
            case LinearAlgebraCalculator.VectorAdd.FORMULA_ID,
                    LinearAlgebraCalculator.VectorSubtract.FORMULA_ID,
                    LinearAlgebraCalculator.VectorDotProduct.FORMULA_ID ->
                    new LinearAlgebraCalculator.VectorInput(
                            text(raw, "first", "Vector u"), text(raw, "second", "Vector v"));
            case LinearAlgebraCalculator.MatrixAdd.FORMULA_ID,
                    LinearAlgebraCalculator.MatrixSubtract.FORMULA_ID,
                    LinearAlgebraCalculator.MatrixProduct.FORMULA_ID ->
                    new LinearAlgebraCalculator.MatrixInput(
                            text(raw, "first", "Matriz A"), text(raw, "second", "Matriz B"));
            case LinearAlgebraCalculator.Determinant.FORMULA_ID,
                    LinearAlgebraCalculator.Inverse.FORMULA_ID,
                    LinearAlgebraCalculator.Rank.FORMULA_ID ->
                    new LinearAlgebraCalculator.SingleMatrixInput(text(raw, "matrix", "Matriz A"));
            case LinearAlgebraCalculator.SolveGauss.FORMULA_ID ->
                    new LinearAlgebraCalculator.SystemInput(
                            text(raw, "matrix", "Matriz A"), text(raw, "constants", "Vector b"));
            case CalculusCalculator.Derivative.FORMULA_ID ->
                    new CalculusCalculator.FunctionInput(text(raw, "expression", "Función"),
                            text(raw, "variable", "Variable"), num(raw, "point", "Punto"),
                            intNum(raw, "order", "Orden"));
            case CalculusCalculator.Antiderivative.FORMULA_ID ->
                    new CalculusCalculator.FunctionPointInput(text(raw, "expression", "Función"),
                            text(raw, "variable", "Variable"), num(raw, "point", "Punto"));
            case CalculusCalculator.DefiniteIntegral.FORMULA_ID ->
                    new CalculusCalculator.DefiniteIntegralInput(text(raw, "expression", "Función"),
                            text(raw, "variable", "Variable"), num(raw, "lower", "Límite inferior"),
                            num(raw, "upper", "Límite superior"));
            case SeriesCalculator.ArithmeticSequence.FORMULA_ID ->
                    new SeriesCalculator.ArithmeticInput(num(raw, "first", "Primer término"),
                            num(raw, "difference", "Diferencia"), intNum(raw, "terms", "Términos"));
            case SeriesCalculator.GeometricSequence.FORMULA_ID ->
                    new SeriesCalculator.GeometricInput(num(raw, "first", "Primer término"),
                            num(raw, "ratio", "Razón"), intNum(raw, "terms", "Términos"));
            case SeriesCalculator.InfiniteGeometric.FORMULA_ID ->
                    new SeriesCalculator.InfiniteGeometricInput(num(raw, "first", "Primer término"),
                            num(raw, "ratio", "Razón"));
            case SeriesCalculator.FiniteSum.FORMULA_ID ->
                    new SeriesCalculator.FiniteSumInput(text(raw, "expression", "Término"),
                            intNum(raw, "start", "Inicio"), intNum(raw, "end", "Fin"));
            case SeriesCalculator.RatioTest.FORMULA_ID, SeriesCalculator.RootTest.FORMULA_ID ->
                    new SeriesCalculator.CriterionInput(num(raw, "limit", "Límite"));
            case SeriesCalculator.LimitComparison.FORMULA_ID ->
                    new SeriesCalculator.ComparisonInput(num(raw, "limit", "Límite"),
                            text(raw, "mode", "Modo de comparación"));
            case SeriesCalculator.SimpleLimit.FORMULA_ID ->
                    new SeriesCalculator.LimitInput(text(raw, "expression", "Función"),
                            text(raw, "variable", "Variable"), num(raw, "point", "Punto"));
            default -> throw new CalcException(ErrorCode.UNKNOWN_FORMULA,
                    "formulaId", "Formula desconocida: " + formulaId);
        };
    }

        private static SolidMetricsCalculator.Input solidInput(Map<String, String> raw,
                                                                                                                        LengthUnit unit,
                                                                                                                        SolidMetricsCalculator.Kind kind)
                        throws CalcException {
                double length = 0;
                double width = 0;
                double height = 0;
                double side = 0;
                double radius = 0;
                double outerRadius = 0;
                double innerRadius = 0;
                int sides = 0;
                switch (kind) {
                        case CUBE, TETRAHEDRON -> side = num(raw, "side", "Lado");
                        case CUBOID -> {
                                length = num(raw, "length", "Largo");
                                width = num(raw, "width", "Ancho");
                                height = num(raw, "height", "Alto");
                        }
                        case REGULAR_PRISM, REGULAR_PYRAMID -> {
                                sides = intNum(raw, "n", "Numero de lados");
                                side = num(raw, "side", "Lado");
                                height = num(raw, "height", "Altura");
                        }
                        case CYLINDER, CONE -> {
                                radius = num(raw, "radius", "Radio");
                                height = num(raw, "height", "Altura");
                        }
                        case CONE_FRUSTUM -> {
                                outerRadius = num(raw, "outerRadius", "Radio mayor");
                                innerRadius = num(raw, "innerRadius", "Radio menor");
                                height = num(raw, "height", "Altura");
                        }
                        case SPHERE, HEMISPHERE -> radius = num(raw, "radius", "Radio");
                }
                return new SolidMetricsCalculator.Input(length, width, height, side, radius,
                                outerRadius, innerRadius, sides, unit);
        }

    private static double num(Map<String, String> raw, String key, String label)
            throws CalcException {
        return InputParser.parseDouble(raw.get(key), key, label);
    }

    private static int intNum(Map<String, String> raw, String key, String label)
            throws CalcException {
        return InputParser.parseInt(raw.get(key), key, label);
    }

    /** Parsea el angulo y lo normaliza a grados. */
    private static double angleDeg(Map<String, String> raw, AngleUnit au) throws CalcException {
        double v = InputParser.parseDouble(raw.get("angle"), "angle", "Angulo");
        return au.toDegrees(v);
    }

    /** Como angleDeg pero para claves con otro nombre (angleC, angleA…). */
    private static double angleDegAs(Map<String, String> raw, String key, String label,
                                     AngleUnit au) throws CalcException {
        double v = InputParser.parseDouble(raw.get(key), key, label);
        return au.toDegrees(v);
    }

    private static String text(Map<String, String> raw, String key, String label)
            throws CalcException {
        return InputParser.parseText(raw.get(key), key, label);
    }

    private static double numOrDefault(Map<String, String> raw, String key, String label,
                                       double defaultValue) throws CalcException {
        return InputParser.parseDoubleOrDefault(raw.get(key), key, label, defaultValue);
    }

    private static long longNum(Map<String, String> raw, String key, String label)
            throws CalcException {
        double v = InputParser.parseDouble(raw.get(key), key, label);
        if (v != Math.rint(v)) {
            throw new CalcException(ErrorCode.INVALID_INPUT, key,
                    label + " debe ser entero. Recibido: " + raw.get(key));
        }
        if (Math.abs(v) > 1_000_000_000L) {
            throw new CalcException(ErrorCode.OVERFLOW, key, label + " demasiado grande.");
        }
        return (long) v;
    }
}

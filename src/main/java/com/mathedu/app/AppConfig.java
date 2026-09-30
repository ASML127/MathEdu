package com.mathedu.app;

import com.mathedu.application.calculation.CalculationService;
import com.mathedu.application.calculation.CalculatorRegistry;
import com.mathedu.application.calculation.FormulaInputFactory;
import com.mathedu.application.history.HistoryService;
import com.mathedu.domain.calculators.analytic.AnalyticGeometryCalculator;
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
import com.mathedu.domain.calculators.geometry.CircleAreaCalculator;
import com.mathedu.domain.calculators.plane.AnnulusAreaCalculator;
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
import com.mathedu.domain.calculators.solid.SolidMetricsCalculator;
import com.mathedu.domain.calculators.stats.StatisticsCalculator;
import com.mathedu.domain.calculators.linear.LinearAlgebraCalculator;
import com.mathedu.domain.calculators.calculus.CalculusCalculator;
import com.mathedu.domain.calculators.series.SeriesCalculator;
import com.mathedu.domain.engine.ExpressionEngine;
import com.mathedu.infrastructure.engine.EvalExEngine;
import com.mathedu.infrastructure.persistence.JsonHistoryRepository;
import com.mathedu.domain.engine.SymbolicEngine;
import com.mathedu.infrastructure.symbolic.LazySymbolicEngine;

/** Configuracion y DI manual (sin framework, YAGNI). */
public final class AppConfig {

    private AppConfig() {
    }

    public static final String APP_NAME = "MathLab Edu";
    public static final String APP_VERSION = "1.0.0";

    /** Motor de expresiones compartido (EvalEx). */
    public static ExpressionEngine buildExpressionEngine() {
        return new EvalExEngine();
    }

    /** Motor simbólico cargado de forma diferida en el primer uso. */
    public static SymbolicEngine buildSymbolicEngine() {
        return new LazySymbolicEngine();
    }

    /** Todas las formulas registradas para los modulos implementados. */
    public static CalculatorRegistry buildRegistry() {
        CalculatorRegistry registry = new CalculatorRegistry();
        registerPlane(registry);
        registerAlgebra(registry, buildExpressionEngine());
        registerTrigonometry(registry);
        registerAnalyticGeometry(registry);
        registerSolids(registry);
        registerStatistics(registry);
        registerLinearAlgebra(registry);
        registerCalculus(registry, buildSymbolicEngine(), buildExpressionEngine());
        registerSeries(registry, buildSymbolicEngine(), buildExpressionEngine());
        return registry;
    }

    private static void registerCalculus(CalculatorRegistry registry, SymbolicEngine symbolic,
            ExpressionEngine numeric) {
        registry.register(new CalculusCalculator.Derivative(symbolic, numeric));
        registry.register(new CalculusCalculator.Antiderivative(symbolic, numeric));
        registry.register(new CalculusCalculator.DefiniteIntegral(symbolic, numeric));
    }

    private static void registerSeries(CalculatorRegistry registry, SymbolicEngine symbolic,
            ExpressionEngine numeric) {
        registry.register(new SeriesCalculator.ArithmeticSequence());
        registry.register(new SeriesCalculator.GeometricSequence());
        registry.register(new SeriesCalculator.InfiniteGeometric());
        registry.register(new SeriesCalculator.FiniteSum(numeric));
        registry.register(new SeriesCalculator.RatioTest());
        registry.register(new SeriesCalculator.RootTest());
        registry.register(new SeriesCalculator.LimitComparison());
        registry.register(new SeriesCalculator.SimpleLimit(symbolic, numeric));
    }

    private static void registerLinearAlgebra(CalculatorRegistry registry) {
        registry.register(new LinearAlgebraCalculator.VectorAdd());
        registry.register(new LinearAlgebraCalculator.VectorSubtract());
        registry.register(new LinearAlgebraCalculator.VectorDotProduct());
        registry.register(new LinearAlgebraCalculator.MatrixAdd());
        registry.register(new LinearAlgebraCalculator.MatrixSubtract());
        registry.register(new LinearAlgebraCalculator.MatrixProduct());
        registry.register(new LinearAlgebraCalculator.Determinant());
        registry.register(new LinearAlgebraCalculator.Inverse());
        registry.register(new LinearAlgebraCalculator.SolveGauss());
        registry.register(new LinearAlgebraCalculator.Rank());
    }

    private static void registerStatistics(CalculatorRegistry registry) {
        registry.register(new StatisticsCalculator.Descriptive());
        registry.register(new StatisticsCalculator.Combination());
        registry.register(new StatisticsCalculator.Permutation());
        registry.register(new StatisticsCalculator.BasicProbability());
        registry.register(new StatisticsCalculator.ConditionalProbability());
        registry.register(new StatisticsCalculator.Binomial());
        registry.register(new StatisticsCalculator.Normal());
        registry.register(new StatisticsCalculator.Poisson());
    }

    private static void registerSolids(CalculatorRegistry registry) {
        for (SolidMetricsCalculator.Kind kind : SolidMetricsCalculator.Kind.values()) {
            registry.register(new SolidMetricsCalculator(kind));
        }
    }

    private static void registerAnalyticGeometry(CalculatorRegistry registry) {
        registry.register(new AnalyticGeometryCalculator.Distance());
        registry.register(new AnalyticGeometryCalculator.Midpoint());
        registry.register(new AnalyticGeometryCalculator.Slope());
        registry.register(new AnalyticGeometryCalculator.LineFromPoints());
        registry.register(new AnalyticGeometryCalculator.PointSlope());
        registry.register(new AnalyticGeometryCalculator.LineFromSlopeIntercept());
        registry.register(new AnalyticGeometryCalculator.GeneralLineConversion());
        registry.register(new AnalyticGeometryCalculator.LineIntersection());
        registry.register(new AnalyticGeometryCalculator.PointLineDistance());
        registry.register(new AnalyticGeometryCalculator.CircleEquation());
    }

    private static void registerPlane(CalculatorRegistry registry) {
        registry.register(new SquareAreaCalculator());
        registry.register(new SquarePerimeterCalculator());
        registry.register(new RectangleAreaCalculator());
        registry.register(new RectanglePerimeterCalculator());
        registry.register(new TriangleBaseHeightCalculator());
        registry.register(new TriangleHeronCalculator());
        registry.register(new TrianglePerimeterCalculator());
        registry.register(new EquilateralAreaCalculator());
        registry.register(new EquilateralPerimeterCalculator());
        registry.register(new IsoscelesAreaCalculator());
        registry.register(new IsoscelesPerimeterCalculator());
        registry.register(new RightAreaCalculator());
        registry.register(new RightPerimeterCalculator());
        registry.register(new ParallelogramAreaCalculator());
        registry.register(new ParallelogramPerimeterCalculator());
        registry.register(new RhombusAreaCalculator());
        registry.register(new RhombusPerimeterCalculator());
        registry.register(new TrapezoidAreaCalculator());
        registry.register(new TrapezoidPerimeterCalculator());
        registry.register(new KiteAreaCalculator());
        registry.register(new KitePerimeterCalculator());
        registry.register(new CircleAreaCalculator());
        registry.register(new CirclePerimeterCalculator());
        registry.register(new SemicircleAreaCalculator());
        registry.register(new SemicirclePerimeterCalculator());
        registry.register(new SectorAreaCalculator());
        registry.register(new SectorArcCalculator());
        registry.register(new AnnulusAreaCalculator());
        registry.register(new AnnulusPerimeterCalculator());
        registry.register(new RegularPolygonAreaCalculator());
        registry.register(new RegularPolygonPerimeterCalculator());
    }

    private static void registerAlgebra(CalculatorRegistry registry, ExpressionEngine engine) {
        registry.register(new AddCalculator());
        registry.register(new SubtractCalculator());
        registry.register(new MultiplyCalculator());
        registry.register(new DivideCalculator());
        registry.register(new ExpressionCalculator(engine));
        registry.register(new PercentCalculator());
        registry.register(new PercentOfCalculator());
        registry.register(new RuleOfThreeCalculator());
        registry.register(new FractionsCalculator());
        registry.register(new PowerCalculator());
        registry.register(new RootCalculator());
        registry.register(new LogCalculator());
        registry.register(new ExpCalculator());
        registry.register(new AbsCalculator());
        registry.register(new LinearEquationCalculator());
        registry.register(new QuadraticEquationCalculator());
        registry.register(new System2x2Calculator());
        registry.register(new System3x3Calculator());
        registry.register(new InequalityCalculator());
        registry.register(new PolyEvaluateCalculator());
        registry.register(new PolyFactorCalculator());
        registry.register(new PolyAddCalculator());
        registry.register(new PolySubtractCalculator());
        registry.register(new PolyMultiplyCalculator());
        registry.register(new PolyDivideCalculator());
        registry.register(new BinomialSquareCalculator());
        registry.register(new BinomialDiffSquareCalculator());
        registry.register(new SumDifferenceCalculator());
    }

    private static void registerTrigonometry(CalculatorRegistry registry) {
        registry.register(new SinCalculator());
        registry.register(new CosCalculator());
        registry.register(new TanCalculator());
        registry.register(new AsinCalculator());
        registry.register(new AcosCalculator());
        registry.register(new AtanCalculator());
        registry.register(new AngleConvertCalculator());
        registry.register(new IdentitiesCalculator());
        registry.register(new PythagorasHypCalculator());
        registry.register(new PythagorasLegCalculator());
        registry.register(new SolveSssCalculator());
        registry.register(new SolveSasCalculator());
        registry.register(new SolveAsaCalculator());
    }

    public static CalculationService buildCalculationService(CalculatorRegistry registry) {
        return new CalculationService(registry);
    }

    public static FormulaInputFactory buildInputFactory() {
        return new FormulaInputFactory();
    }

    public static HistoryService buildHistoryService() {
        return new HistoryService(new JsonHistoryRepository(JsonHistoryRepository.defaultPath()));
    }
}

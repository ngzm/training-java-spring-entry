package jp.sample.bookmgr.biz.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 書籍管理アプリケーションのアスペクト処理実装クラス
 * 
 * @author 長住@NTT-AT
 * @version 1.0
 */
@Aspect
@Component
public class BookAspect {
	/**
	 * ロガー
	 */
	private static final Logger logger = LoggerFactory.getLogger(BookAspect.class);

	/**
	 * jp.sample.bookmgr.biz パッケージの全てのクラス・メソッドの開始前に呼ばれる処理
	 * APO対象となるクラスとメソッドの名前を取得し、メソッド開始ログを出力する
	 * 
	 * @param jp JoinPointオブジェクト
	 */ 
	@Before("execution(* jp.sample.bookmgr.biz..*(..))")
	public void beforeMethod(JoinPoint jp) {
		// AOP対象のクラス名を取得
		String className = jp.getTarget().getClass().getName();
		// AOP対象のメソッド名を取得
		String methodName = jp.getSignature().getName();
		// ログ出力
		logger.info("Before: " + className + "#" + methodName + " START!!");
	}

	/**
	 * jp.sample.bookmgr.biz パッケージの全てのクラス・メソッドが終了する直前に呼ばれる処理
	 * APO対象となるメソッドの名前を取得し、メソッド終了ログを出力する
	 * 
	 * @param jp JoinPointオブジェクト
	 */ 
	@After("execution(* jp.sample.bookmgr.biz..*(..))")
	public void afterMethod(JoinPoint jp) {
		// AOP対象のクラス名を取得
		String className = jp.getTarget().getClass().getName();
		// AOP対象のメソッド名を取得
		String methodName = jp.getSignature().getName();
		// ログ出力
		logger.info("After: " + className + "#" + methodName + " END!!");
	}

	/**
	 * jp.sample.bookmgr.biz パッケージの全てのクラス・メソッドが例外をスローした時に呼ばれる処理
	 * APO対象となるクラスとメソッドの名前を取得し、例外メッセージを出力する
	 * 
	 * @param jp JoinPointオブジェクト
	 */ 
	@AfterThrowing(value="execution(* jp.sample.bookmgr.biz..*(..))", throwing="ex")
	public void afterThrowingMethod(JoinPoint jp, Exception ex) {
		// AOP対象のクラス名を取得
		String className = jp.getTarget().getClass().getName();
		// AOP対象のメソッド名を取得
		String methodName = jp.getSignature().getName();
		// Exceptionメッセージを取得
		String mes = ex.toString();
		// ログ出力
		logger.info("AfterThrowing: " + className + "#" + methodName + " Throw(\"" + mes + "\")");
	}
}

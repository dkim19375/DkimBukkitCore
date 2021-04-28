package me.dkim19375.dkim19375core.annotation

@Target(AnnotationTarget.CLASS,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.FILE)
@Retention(AnnotationRetention.SOURCE)
/**
 * An annotation class used for APIs for
 * actions such as suppressing 'unused' warnings
 */
annotation class API
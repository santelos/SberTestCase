package ru.pstroganov.sbertest.database.specifications;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Стандартные спецификации
 */

public abstract class BasicSpecifications<T> {

    /**
     * Замена значения True при использовании {@link Specification}<br>
     * То есть,<br>
     * {@code getTrue().and(Specification<>) == Specification<>}
     *
     * @return Эквивалент True для Спецификаций
     * @param <T>
     * @return
     */
    public static <T> Specification<T> getTrue(){
        return (r,cq,cb)-> cb.and();
    }

    /**
     * Замена значения False при использовании {@link Specification}<br>
     * То есть,<br>
     * {@code getFalse().and(Specification<>) == getFalse()}
     *
     * @return Эквивалент False для Спецификаций
     */
    public static <T> Specification<T> getFalse(){
        return (r,cq,cb)-> cb.or();
    }

    /**
     * Компонует спецификации в одну используя AND.
     *
     * @param specifications Спецификации для компановки
     * @return Одиночная спецификация, результат компановки
     */
    public static <T> Specification<T> composeAnd(List<Specification<T>> specifications){
        // Если нужно соединить спецификации, которых нет, то вместо соединения говорим, что это будет пустое место
        if(specifications.isEmpty()) return getTrue();

        Specification<T> composedSpec = specifications.get(0);

        // Составляем спецификации через AND
        for(int i=1;i<specifications.size();composedSpec.and(specifications.get(i++)));

        return composedSpec;
    }
}

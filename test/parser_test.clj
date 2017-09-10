(ns parser-test
  (:require [clojure.test :refer :all]
            [parser :refer :all]
  )
)


(def small_db "
  varon ( juan).
  mujer(vero ).
")

(def rules_db "
  varon(jay).
  padre(jay, pepe).
  hijo(X, Y) :- varon(X), padre(Y, X)
")


(deftest parser-general-test
  (testing "'clean' removes whitespaces"
    (is
      (= (clean "qwe\t123\n/() zxc") "qwe123/()zxc")
    )
  )
)


(deftest parser-facts-test
  (testing "get array of facts"
    (is 
      (= (parseFacts small_db) ["varon(juan)" "mujer(vero)"])
    )
  )

  (testing "facts assertion returns facts if valid"
    (let 
      [facts ["planta(arbol)" "animal(perro)"]]

      (is (=
          (assertFacts facts)
          facts
      ))
    )
  )

  (testing "facts assertion returns nil if invalid"
    (let
      [facts ["planta(arbol)" "animal"]]

      (is (nil? (assertFacts facts)))
    )
  )
)


(deftest parser-rules-test
  (testing "can make rule out of line"
    (is
      (= 
        (makeRule "hijo(A,B):-varon(A),padre(B,A)") 
        ["hijo(A,B)" ["varon(A)" "padre(B,A)"]]
      )
    )
  )

  (testing "get array of rules"
    (is
      (= (parseRules rules_db) [["hijo(X,Y)" ["varon(X)" "padre(Y,X)"]]])
    )
  )

)

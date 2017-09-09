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

(deftest parser-test
  (testing
    "'clean' removes whitespaces"
    (is
      (= (clean "qwe\t123\n/() zxc") "qwe123/()zxc")
    )
  )

  (testing
    "get array of facts"
    (is 
      (= (parseFacts small_db) ["varon(juan)" "mujer(vero)"])
    )
  )

  (testing
    "can make rule out of line"
    (is
      (= 
        (makeRule "hijo(A,B):-varon(A),padre(B,A)") 
        ["hijo(A,B)" ["varon(A)" "padre(B,A)"]]
      )
    )
  )

  (testing
    "get array of rules"
    (is
      (= (parseRules rules_db) [["hijo(X,Y)" ["varon(X)" "padre(Y,X)"]]])
    )
  )
)

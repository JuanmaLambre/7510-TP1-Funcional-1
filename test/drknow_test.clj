(ns drknow-test
  (:require [clojure.test :refer :all]
            [drknow :refer :all]
            [parser]
  )
)

(def sampleDB "
  jazz(zenon).
  musico(zenon).
  musico(lebon).
  jazzista(X) :- jazz(X), musico(X).
  colegas(X, Y) :- musico(X), musico(Y).
")

(deftest drknow-facts-test
  (testing "check true fact"
    (is (= (isFact ["mujer(luna)" "varon(sol)"] "mujer(luna)") true))
  )

  (testing "check false fact"
    (is (= (isFact ["superheroe(batman)"] "superheroe(pabloLescano)") false))
  )
)


(deftest drknow-rules-test
  (let
    [
      facts (parser/parseFacts sampleDB)
      rules (parser/parseRules sampleDB)
      query "colegas(zenon, lebon)"
    ]

    (testing "get variables from rule"
      (is (=
          (getVariables "proposition(X,Y)")
          ["X" "Y"]
      ))
    )

    (testing "look for correct rule"
      (is (=
          (lookRule query rules)
          (nth rules 1)
      ))
    )

    (testing "replace two query params"
      (is (=
          (replaceParams ["A" "B"] ["juan" "pepe"] ["lefact1(A)" "lefact2(B,A)"])
          ["lefact1(juan)" "lefact2(pepe,juan)"]
      ))
    )

    (testing "replace repeated query params"
      (is (=
          (replaceParams 
            ["X" "Y" "Z"]
            ["two" "one" "one"]
            ["onefact(Y,Z,X)"]
          )
          ["onefact(one,one,two)"]
      ))
    )

    (testing "check true rule"
      (is (= 
          (appliesRule
            (first rules)
            facts
            "jazzista(zenon)"
          )
          true
      ))
    )

    (testing "check false rule"
      (is (= 
          (appliesRule (first rules) facts "jazzista(lebon)")
          false
      ))
    )
  )
)
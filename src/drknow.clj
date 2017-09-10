(ns drknow
  (:require
    [clojure.string :as str]
    [parser]
  )
)

; Starving minds, welcome to Dr. Know! Where fast food for thought 
;  is served up 24 hours a day, in 40,000 locations nationwide.
;  Ask. Dr. Know - there's nothing I don't!


(defn replaceParams [ruleParams queryParams ruleFacts]
  (map
    (fn [ruleFact]
      (reduce
        (fn [qp1 qp2]
          (str/replace-first
            qp1
            qp2
            (nth queryParams (.indexOf ruleParams qp2))
          )
        )
        ruleFact
        ruleParams
      )
    )
    ruleFacts
  )
)

(defn getVariables [proposition]
  (subvec
    (str/split proposition #"(\(|,|\))")
    1
  )
)

(defn lookRule [query rules]
  (first (filter
      #(.startsWith 
        (nth % 0)
        (first (str/split query #"\("))
      )
      rules
  ))
)

(defn isFact [facts lefact]
  (= 
    (some #(= % lefact) facts) 
    true
  )
)

(defn appliesRule [rule facts query]
  (=
    (every? 
      #(isFact facts %)
      (replaceParams
        (getVariables (nth rule 0))
        (getVariables query)
        (nth rule 1)
      )
    )
    true
  )
)

(defn ask [query facts rules]
  (let 
    [daRule (lookRule query rules)]

    (if 
      (and
        (not (nil? facts))
        (not (nil? rules))
        (parser/validFact query)
      )

      (or
        (isFact facts query)
        (and
          (not (nil? daRule))
          (appliesRule daRule facts query)
        )
      )
    )
  )
)
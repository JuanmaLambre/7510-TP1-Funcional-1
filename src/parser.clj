(ns parser
  (require [clojure.string :as str])
)

(defn clean [string]
  (str/replace string #"(\ |\t|\n)" "")  
)

(defn makeRule [rulestr]
  [
    (get (str/split rulestr #":-") 0)
    (str/split
      (str/replace 
        (get (str/split rulestr #":-") 1)
        "),"
        ")!"
      )
      #"!"
    )
  ]
)

(defn validFact [fact]
  (not (nil? (re-matches #"^[a-z0-9]+\([a-z0-9,]+\)$" fact)))
)

(defn assertFacts [facts]
  (if 
    (every?
      true?
      (map validFact facts)
    )
    facts
  )
)

(defn parseFacts [data]
  (assertFacts
    (map 
      clean 
      (filter
        #(and 
          (not (.contains % ":-"))
          (not (str/blank? %))
        )
        (str/split data #"\.")
      )
    )
  )
)

(defn parseRules [data]
  (map 
    makeRule
    (map
      clean
      (filter
        #(and 
          (.contains % ":-")
          (not (str/blank? %))
        )
        (str/split data #"\.")
      )
    )
  )  
)
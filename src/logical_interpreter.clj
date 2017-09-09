(ns logical-interpreter
  (:require [parser :refer :all])
)

(defn evaluate-query
  "Returns true if the rules and facts in database imply query, false if not. If
  either input can't be parsed, returns nil"
  [data query]

  (def facts (parseFacts data))
  (def rules (parseRules data))

  

  nil)

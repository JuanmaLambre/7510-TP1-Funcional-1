(ns logical-interpreter
  (:require 
    [parser]
    [drknow]
  )
)

(defn evaluate-query
  "Returns true if the rules and facts in database imply query, false if not. If
  either input can't be parsed, returns nil"
  [data query]

  (def facts (parser/parseFacts data))
  (def rules (parser/parseRules data))

  (drknow/ask (parser/clean query) facts rules)
)

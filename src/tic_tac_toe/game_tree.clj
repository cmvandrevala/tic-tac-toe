(ns tic-tac-toe.game-tree)

(defn node [params] params)

(defn add-child [params parent-node]
  (if (nil? (:children parent-node))
    (assoc parent-node :children [params])
    (assoc parent-node :children (into [] (concat [params] (:children parent-node))))))

(defn values-of-children [node]
  (let [children (get node :children)]
    (vec (map #(if (nil? %) (do (println (values-of-children %)) (values-of-children %)) %) (vec (map #(get % :value) children))))))

(defn score [tree]
  (if (nil? (get tree :value))
    (apply max (values-of-children tree))
    (get tree :value)))

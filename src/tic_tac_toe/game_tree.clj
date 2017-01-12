(ns tic-tac-toe.game-tree)

(defn node [params] params)

(defn add-child [params parent-node]
  (if (nil? (:children parent-node))
    (assoc parent-node :children [params])
    (assoc parent-node :children (into [] (concat [params] (:children parent-node))))))

(defn values-of-children [node]
  (let [children (:children node)]
    (vec (map #(if (nil? %) (values-of-children %) %) (vec (map #(:value %) children))))))

(defn score
  ([tree] (score tree 0))
  ([tree level]
   (if (and (nil? (:value tree)) (even? level))
      (apply max (map #(score % (inc level)) (:children tree)))
      (if (and (nil? (:value tree)) (odd? level))
        (apply min (map #(score % (inc level)) (:children tree)))
        (get tree :value)))))

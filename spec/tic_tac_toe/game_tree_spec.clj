(ns tic-tac-toe.game-tree-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.game-tree :refer :all]))

(def nil-node (node {:value nil}))
(defn test-node [value] (node {:value value}))

(defn add-many-children [parent-node values]
  (loop [children (reverse values) root parent-node]
    (if (empty? children)
      root
      (recur (rest children) (add-child (test-node (first children)) root)))))

(describe "building a game tree"

  (it "builds a root node"
    (should= {:value 2} (node {:value 2})))

  (it "adds one child node to a node"
    (should= {:value 2 :children [{:value 6}]} (add-child {:value 6} (node {:value 2}))))

  (it "adds two children to a node"
    (let [root (node {:value 2})
          root-with-children (add-child (node {:value 6}) (add-child (node {:value 8}) root))]
        (should= {:value 2 :children [{:value 6} {:value 8}]} root-with-children))))

(describe "values of children"

  (it "returns an empty vector if there are no children"
    (should= [] (values-of-children nil-node)))

  (it "returns a single value if there is one child"
    (let [game-tree (add-child (test-node 6) nil-node)]
      (should= [6] (values-of-children game-tree))))

  (it "returns two values if there are two children"
    (let [game-tree (add-many-children nil-node [6 8])]
        (should= [6 8] (values-of-children game-tree))))

  (it "returns many values if there are many children"
    (let [game-tree (add-many-children nil-node [16 81 12])]
      (should= [16 81 12] (values-of-children game-tree)))))

(describe "traversing a game tree"

  (it "returns the value of a node if it is the only one in the tree"
      (let [game-tree (test-node 5)]
        (should= 5 (score game-tree))))

  (it "returns the value of a node, even if it is negative"
      (let [game-tree (test-node -1)]
        (should= -1 (score game-tree))))

  (it "returns the value of the single child of the root"
      (let [game-tree (add-child (test-node 12) nil-node)]
        (should= 12 (score game-tree))))

  (it "returns the maximum value of the child nodes when the maximum value is first"
      (let [game-tree (add-many-children nil-node [1 -1])]
        (should= 1 (score game-tree))))

  (it "returns the maximum value of the child nodes when the maximum value is not first"
      (let [game-tree (add-many-children nil-node [1 13 -1])]
        (should= 13 (score game-tree))))

  (it "returns a negative value if it is the maximum value of the children"
      (let [game-tree (add-many-children nil-node [-1 -13 -10])]
        (should= -1 (score game-tree))))

  (it "gracefully handles duplicate values"
      (let [game-tree (add-many-children nil-node [-1 -1 -1])]
        (should= -1 (score game-tree)))))

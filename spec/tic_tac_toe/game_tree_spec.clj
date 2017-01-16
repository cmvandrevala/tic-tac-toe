(ns tic-tac-toe.game-tree-spec
  (:require [speclj.core :refer :all]
            [tic-tac-toe.game-tree :refer :all]))

(defn test-node [value] (node {:value value}))

(describe "building a game tree"

  (it "builds a root node"
    (should= {:value 2} (node {:value 2})))

  (it "adds one child node to a node"
    (should= {:value 2 :children [{:value 6}]} (add-child {:value 6} (node {:value 2}))))

  (it "adds two children to a node"
    (let [root (node {:value 2})
          child1 (node {:value 6})
          child2 (node {:value 8})
          root-with-children (add-child child1 (add-child child2 root))]
        (should= {:value 2 :children [{:value 6} {:value 8}]} root-with-children)))

  (it "creates a deeply nested tree"
    (let [root (node {:value 3})
          child (node {:value 4})
          grandchild (node {:value 5})
          root-with-grandchild (add-child (add-child grandchild child) root)]
        (should= {:value 3 :children [{:value 4 :children [{:value 5}]}]} root-with-grandchild))))

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
      (should= [16 81 12] (values-of-children game-tree))))

  (it "returns the values of the children of a non-root node"
    (let [child-node (add-many-children nil-node [1 2 3 4 5])
          root-node (add-child child-node nil-node)]
      (should= [1 2 3 4 5] (values-of-children (first (:children root-node)))))))

(describe "scoring a game tree"

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
      (should= -1 (score game-tree))))

  (it "returns the value of a grandchild if it is the only node with a value"
    (let [game-tree (add-child (add-child (test-node 3) nil-node) nil-node)]
      (should= 3 (score game-tree))))

  (it "returns the minimum value of the grandchildren if there are no other values"
    (let [game-tree (add-child (add-many-children nil-node [5 4 0 1 2 3]) nil-node)]
      (should= 0 (score game-tree))))

  (it "returns the score from a child if it yields a better score than the grandchildren"
    (let [top-node (add-child (add-many-children nil-node [1 -1]) nil-node)
          game-tree (add-child (test-node 0) top-node)]
      (should= 0 (score game-tree))))

  (it "returns the score from a specific set of grandchildren if it is the maximal score"
    (let [top-node (add-child (add-many-children nil-node [5 -5]) nil-node)
          game-tree (add-child (add-many-children nil-node [10 -10]) top-node)]
      (should= -5 (score game-tree))))

  (it "scores a game tree with two moves left (tie over loss)"
    (let [game-tree {:value nil :children [{:player :player-two :cell 6 :value nil :children [{:player :player-one :cell 1 :value -1}]} {:player :player-two :cell 1 :value nil :children [{:player :player-one :cell 6 :value 0}]}]}]
      (should= 0 (score game-tree))))

  (it "scores a game tree with two moves left (win over loss)"
    (let [game-tree {:value nil :children [{:player :player-two :cell 6 :value nil :children [{:player :player-one :cell 1 :value 1}]} {:player :player-two :cell 1 :value nil :children [{:player :player-one :cell 6 :value -1}]}]}]
      (should= 1 (score game-tree))))

  (it "scores a game tree with two moves left (two potential wins)"
    (let [game-tree {:value nil :children [{:player :player-two :cell 6 :value nil :children [{:player :player-one :cell 1 :value 1}]} {:player :player-two :cell 1 :value nil :children [{:player :player-one :cell 6 :value 1}]}]}]
      (should= 1 (score game-tree))))

  (it "scores a complicated game tree in the correct manner"
    (let [c1 (add-many-children nil-node [5 1 -5])
          c2 (add-many-children nil-node [-1 2])
          c3 (add-many-children nil-node [3 1])
          c4 (add-many-children nil-node [0])
          c5 (add-many-children nil-node [-1 2])
          c6 (add-many-children nil-node [3 -5])
          b1 (add-child c1 (add-child c2 nil-node))
          b2 (add-child c3 nil-node)
          b3 (add-child c4 (add-child c5 (add-child c6 nil-node)))
          a (add-child b1 (add-child b2 (add-child b3 nil-node)))]
        (should= 3 (score a)))))

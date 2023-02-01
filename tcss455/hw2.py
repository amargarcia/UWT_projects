# Homework 2
# name: Martine De Cock
# description: Training and testing decision trees with discrete-values attributes

import sys
import math
import pandas as pd
import operator
from collections import Counter

class DecisionNode:

    # A DecisionNode contains an attribute and a dictionary of children. 
    # The attribute is either the attribute being split on, or the predicted label if the node has no children.
    def __init__(self, attribute):
        self.attribute = attribute
        self.children = {}

    # Visualizes the tree
    def display(self, level = 0):
        if self.children == {}: # reached leaf level
            print(": ", self.attribute, end="")
        else:
            for value in self.children.keys():
                prefix = "\n" + " " * level * 4
                print(prefix, self.attribute, "=", value, end="")
                self.children[value].display(level + 1)
     
    # Predicts the target label for instance x
    def predicts(self, x):
        if self.children == {}: # reached leaf level
            return self.attribute
        value = x[self.attribute]
        subtree = self.children[value]
        return subtree.predicts(x)


# Illustration of functionality of DecisionNode class
def funTree():
    myLeftTree = DecisionNode('humidity')
    myLeftTree.children['normal'] = DecisionNode('no')
    myLeftTree.children['high'] = DecisionNode('yes')
    myTree = DecisionNode('wind')
    myTree.children['weak'] = myLeftTree
    myTree.children['strong'] = DecisionNode('no')
    return myTree


def id3(examples, target, attributes):

    examplesList = examples.loc[:,target].tolist()

    if examplesList.count(examplesList[0]) == len(examplesList):
        return DecisionNode(examplesList[0])
        
    elif len(attributes) == 0:  
        exampleCount = {}
        for example in examplesList:
            if example not in exampleCount.keys(): 
                exampleCount[example] = 1
            exampleCount[example] += 1
        sortedCount = sorted(exampleCount.items(), key=operator.itemgetter(1), reverse=True) # key required for republican
        return DecisionNode(sortedCount[0][0])

    else:      
        finalGain = 0.0
        attrIndex = 0
        
        for num in range(len(attributes)):
            entropy = getEntropy(examples, target)
            counter = Counter(examples.loc[:, attributes[num]])
            newEntropy = 0.0
            
            for key in counter:
                newEntropy += (counter[key] / sum(counter.values())) * getEntropy(extract(examples, attributes[num], key), target)
                
            newGain = (entropy - newEntropy)

            if newGain > finalGain:
                finalGain = newGain
                attrIndex = num

        topAttr = attributes[attrIndex]
        tree = DecisionNode(topAttr)
        counter2 = Counter(examples.loc[:, topAttr])
        values = []

        for key in counter2:
            if key not in values:
                values.append(key)

        for value in values:
            newExample = extract(examples, topAttr, value)
            newAttr = attributes[:]
            newAttr.remove(topAttr)
            subtree = id3(newExample, target, newAttr)
            tree.children[value] = subtree

    #tree = funTree()
    return tree

# returns entropy
def getEntropy(examples, target):
    counter = Counter(examples.loc[:,target])
    sum = 0
    for num in counter:
        sum += -1.0 * (counter[num] / len(examples)) * math.log(counter[num] / len(examples), 2)
    return sum

#
def extract(examples, attr, val):
    newExample = examples.loc[examples[attr] == val]
    del newExample[attr]

    return newExample



####################   MAIN PROGRAM ######################

# Reading input data
train = pd.read_csv(sys.argv[1])
test = pd.read_csv(sys.argv[2])
target = sys.argv[3]
attributes = train.columns.tolist()
attributes.remove(target)

# Learning and visualizing the tree
tree = id3(train,target,attributes)
tree.display()

# Evaluating the tree on the test data
correct = 0
for i in range(0,len(test)):
    if str(tree.predicts(test.loc[i])) == str(test.loc[i,target]):
        correct += 1
print("\nThe accuracy is: ", correct/len(test))
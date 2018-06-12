# SNS
An implementation of the stemming algorithm for text retrieval presented in:

Paik, J. H., Pal, D., & Parui, S. K. (2011). *A Novel Corpus-Based Stemming Algorithm using Co-occurrence Statistics*. Proceedings of the 34th International ACM SIGIR Conference on Research and Development in Information Retrieval.
## Algorithm overview
The goal of SNS is to group morphologically related words in different clusters, using the frequencies of every word in each document. The stemmer does not make use of language-specific rules, but only information about occurrences of the word: it is language-independent.

The algorithm consists of three main parts:
1. computing co-occurrence strength of words pairs, in order to create a graph where co-occurring words are connected (if they meet certain conditions on prefixes and suffixes);
2. re-calculating co-occurrence strength considering neighbours in the graph;
3. grouping words considering the new co-occurrence computed.

In order to achieve all of these steps, one only needs the occurrences of each word on all documents, which are used to compute the initial co-occurrence strength. The other steps of the algorithm are based on the computation of these data. Specifically, the re-calculating of the co-occurrence strength is achieved using the following formula:

![img](http://latex.codecogs.com/svg.latex?RCO%28a%2Cb%29%3DCO%28a%2Cb%29%2B%5Csum_%7Bc%5CinN_%7Ba%2Cb%7D%7Dmin%28CO%28a%2Cc%29%2CCO%28c%2Cb%29%29%5Ccdot0.5)

## Software design and implementation
### Technologies
I chose Java to develop the project. This programming language has many opensource libraries and large active communities. Moreover, programs written in Java are portable as they are platform-independent. Finally, this stemmer is intended to function as a plugin for the [Terrier IR Platform](http://terrier.org/), which is also written in Java.

In order to make the management of the Java-based project easier, I decided to use [Maven](https://maven.apache.org/). This tool provides a uniform build system and some guidelines for best practices development. Furthermore, Maven is exceptionally good at managing external dependencies.
### Design choices
I decided to respect a very important principle of software engineering: the design should be specific to the problem that one is facing, but also general enough for future requirements. I want to avoid the need for a redesign in case someone wants to use and extend the software.

Breaking up the code into modules helps to organize large code bases and makes programs easier to understand. Modules are helpful also for creating libraries that can be imported and used in different applications which share some functionalities. I created many standalone modules that can be used and extended even outside the context of this project.

![img](https://www.imageupload.co.uk/images/2018/06/12/com.stemby.png)

I split the code into two main packages:
- `code.stemby.commons` General purpose package that can be used for a variety of tasks
- `com.stemby.ir` Package that contains classes and interfaces closely tied to IR

Each package is further divided into sub-packages, organized by purpose: `util` provides utilities for data structures, `algorithms` contains a collection of algorithms and `io` provides support to input and output operations.

The main part of my work is contained in the package `com.stemby.ir.algorithms.stemming.sns`, whose function is to implement the SNS algorithm.

### Extensibility and usability
First of all, I defined a program skeleton of the algorithm using the *template method* pattern. Specifically, the main steps of the algorithm (co-occurrences calculation, creation of the adjacency matrix, graph clustering) can be overridden by subclasses to allow a different implementation while ensuring that the overarching algorithm is still followed. I implemented this pattern using abstract methods (see class `com.stemby.ir.algorithms.stemming.sns.AbstractSnsStemmer.java`).

I ensured that the implementation of the algorithm would be easily extended when someone decides to implement some steps in a different way. In order to achieve this, I used th *strategy* pattern. It enables the algorithm's behaviour to be selected at runtime. I implemented different strategies to use depending on the context in which one wants to use the application (e.g. one could use different strategies depending on the available resources). The strategies are available in `com.stemby.ir.algorithms.stemming.sns.strategy`.

Finally, I realized that the stemmer was not usable. Indeed, it had a constructor with many parameters and it was difficult to remember the required order of the parameters. I created the stemmer object using the *builder* pattern. This results in code that is easy to write and very easy to read and understand. Moreover, this pattern is flexible and it us easy to add other parameters to it. The builder can be found at `com.stemby.ir.algorithms.stemming.sns.builder`.

## Evaluation
One can use this software as a plugin for Terrier to figure out if SNS improves the effectiveness of the overall system and to try to reproduce the results described in the paper.

(ns spica.resource.base
  (:require [datomic.api :as d]
            [spica.resource.helpers :as helpers]
            [camel-snake-kebab.core :refer :all]))

;; CLASS
(defn ident [self]
  (first (keys (.manifest self))))

(defn schema [self]
  (helpers/manifest->schema (.manifest self)))

(defn all
  ([self] (all self {} (d/db (d/connect helpers/default-db-uri))))
  ([self options] (all self options (d/db (d/connect helpers/default-db-uri))))
  ([self {:keys [return] :or {return :records} :as options} db]
   (let [ids (d/q '[:find [?eid ...]
                    :in $ ?attribute
                    :where
                    [?eid ?attribute ?uuid]]
                  db (helpers/resource-attribute (.ident self)))]
     (condp = return
       :ids ids
       :entities (map #(d/entity db %) ids)
       :records (map #(d/entity db %) ids)))))

(defn one
  ([self] (one self {} (d/db (d/connect helpers/default-db-uri))))
  ([self options] (one self options (d/db (d/connect helpers/default-db-uri))))
  ([self {:keys [return] :or {return :record} :as options} db]
   (let [id (d/q '[:find ?eid .
                   :in $ ?attribute
                   :where
                   [?eid ?attribute ?uuid]]
                 db (helpers/resource-attribute (.ident self)))]
     (condp = return
       :id id
       :entity (d/entity db id)
       :record (d/entity db id)))))

(defn where
  ([self params] (where self params {} (d/db (d/connect helpers/default-db-uri))))
  ([self params options] (where self params options (d/db (d/connect helpers/default-db-uri))))
  ([self params {:keys [return] :or {return :records} :as options} db]
   (let [tuple1 (nth (into [] params) 0 nil)
         tuple2 (nth (into [] params) 1 nil)
         tuple3 (nth (into [] params) 2 nil)
         tuple4 (nth (into [] params) 3 nil)

         ids (cond
               (= 1 (count params))
               (d/q '[:find [?eid ...]
                      :in $ ?attribute1 ?value1
                      :where
                      [?eid ?attribute1 ?value1]]
                    db (helpers/resource-attribute (.ident self) (first tuple1)) (last tuple1))

               (= 2 (count params))
               (d/q '[:find [?eid ...]
                      :in $ ?attribute1 ?value1 ?attribute2 ?value2
                      :where
                      [?eid ?attribute1 ?value1]
                      [?eid ?attribute2 ?value2]]
                    db
                    (helpers/resource-attribute (.ident self) (first tuple1)) (last tuple1)
                    (helpers/resource-attribute (.ident self) (first tuple2)) (last tuple2))

               (= 3 (count params))
               (d/q '[:find [?eid ...]
                      :in $ ?attribute1 ?value1 ?attribute2 ?value2 ?attribute3 ?value3
                      :where
                      [?eid ?attribute1 ?value1]
                      [?eid ?attribute2 ?value2]
                      [?eid ?attribute3 ?value3]]
                    db
                    (helpers/resource-attribute (.ident self) (first tuple1)) (last tuple1)
                    (helpers/resource-attribute (.ident self) (first tuple2)) (last tuple2)
                    (helpers/resource-attribute (.ident self) (first tuple3)) (last tuple3))

               (= 4 (count params))
               (d/q '[:find [?eid ...]
                      :in $ ?attribute1 ?value1 ?attribute2 ?value2 ?attribute3 ?value3 ?attribute4 ?value4
                      :where
                      [?eid ?attribute1 ?value1]
                      [?eid ?attribute2 ?value2]
                      [?eid ?attribute3 ?value3]
                      [?eid ?attribute4 ?value4]]
                    db
                    (helpers/resource-attribute (.ident self) (first tuple1)) (last tuple1)
                    (helpers/resource-attribute (.ident self) (first tuple2)) (last tuple2)
                    (helpers/resource-attribute (.ident self) (first tuple3)) (last tuple3)
                    (helpers/resource-attribute (.ident self) (first tuple4)) (last tuple4)))]
     (condp = return
       :ids ids
       :entities (map #(d/entity db %) ids)
       :records (map #(d/entity db %) ids)))))

(defn detect
  ([self params] (detect self params {} (d/db (d/connect helpers/default-db-uri))))
  ([self params options] (detect self params options (d/db (d/connect helpers/default-db-uri))))
  ([self params {:keys [return] :or {return :record} :as options} db]
   (let [tuple1 (nth (into [] params) 0 nil)
         tuple2 (nth (into [] params) 1 nil)
         tuple3 (nth (into [] params) 2 nil)
         tuple4 (nth (into [] params) 3 nil)

         id (cond
              (= 1 (count params))
              (d/q '[:find ?eid .
                     :in $ ?attribute1 ?value1
                     :where
                     [?eid ?attribute1 ?value1]]
                   db (helpers/resource-attribute (.ident self) (first tuple1)) (last tuple1))

              (= 2 (count params))
              (d/q '[:find ?eid .
                     :in $ ?attribute1 ?value1 ?attribute2 ?value2
                     :where
                     [?eid ?attribute1 ?value1]
                     [?eid ?attribute2 ?value2]]
                   db
                   (helpers/resource-attribute (.ident self) (first tuple1)) (last tuple1)
                   (helpers/resource-attribute (.ident self) (first tuple2)) (last tuple2))

              (= 3 (count params))
              (d/q '[:find ?eid .
                     :in $ ?attribute1 ?value1 ?attribute2 ?value2 ?attribute3 ?value3
                     :where
                     [?eid ?attribute1 ?value1]
                     [?eid ?attribute2 ?value2]
                     [?eid ?attribute3 ?value3]]
                   db
                   (helpers/resource-attribute (.ident self) (first tuple1)) (last tuple1)
                   (helpers/resource-attribute (.ident self) (first tuple2)) (last tuple2)
                   (helpers/resource-attribute (.ident self) (first tuple3)) (last tuple3))

              (= 4 (count params))
              (d/q '[:find ?eid .
                     :in $ ?attribute1 ?value1 ?attribute2 ?value2 ?attribute3 ?value3 ?attribute4 ?value4
                     :where
                     [?eid ?attribute1 ?value1]
                     [?eid ?attribute2 ?value2]
                     [?eid ?attribute3 ?value3]
                     [?eid ?attribute4 ?value4]]
                   db
                   (helpers/resource-attribute (.ident self) (first tuple1)) (last tuple1)
                   (helpers/resource-attribute (.ident self) (first tuple2)) (last tuple2)
                   (helpers/resource-attribute (.ident self) (first tuple3)) (last tuple3)
                   (helpers/resource-attribute (.ident self) (first tuple4)) (last tuple4)))]

     (if id
       (condp = return
         :id id
         :entity (d/entity db id)
         :record (d/entity db id))
       nil))))

(defn lookup
  ([self id] (lookup self id {} (d/db (d/connect helpers/default-db-uri))))
  ([self id options] (lookup self id options (d/db (d/connect helpers/default-db-uri))))
  ([self id {:keys [return] :or {return :record} :as options} db]
   (let [ent (d/entity db id)]
     (if-not (empty? (into [] ent))
       (condp = return
         :id (:db/id ent)
         :entity ent
         :record ent)
       nil))))

(defn destroy-all!
  ([self] (destroy-all! self (d/connect helpers/default-db-uri)))
  ([self connection]
   (let [tx-data (mapv #(vector :db.fn/retractEntity %) (all self {:return :ids}))]
     (d/transact connection tx-data))))

(defn destroy-where!
  ([self params] (destroy-where! self params (d/connect helpers/default-db-uri)))
  ([self params connection]
   (let [tx-data (mapv #(vector :db.fn/retractEntity %) (where self params {:return :ids}))]
     (d/transact connection tx-data))))

(defn build
  ([self params] (build self params {} (d/connect helpers/default-db-uri)))
  ([self params options] (build self params options (d/connect helpers/default-db-uri)))
  ([self params options connection]
   (let [tempid (d/tempid :db.part/user)
         tx-fragment (reduce-kv (fn [memo key value] (assoc memo (helpers/resource-attribute (.ident self) key) value))
                                {} params)
         built-record (merge
                       {:db/id tempid
                        (helpers/resource-attribute (.ident self) :uuid) (d/squuid)}
                       tx-fragment)]

     built-record)))

(defn create
  ([self params] (create self params {} (d/connect helpers/default-db-uri)))
  ([self params {:keys [return] :or {return :record} :as options}] (create self params options (d/connect helpers/default-db-uri)))
  ([self params {:keys [return] :or {return :record} :as options} connection]
   (let [tempid (d/tempid :db.part/user)
         tx-fragment (reduce-kv (fn [memo key value] (assoc memo (helpers/resource-attribute (.ident self) key) value))
                                {} params)
         tx-data (vector (merge
                          {:db/id tempid
                           (helpers/resource-attribute (.ident self) :uuid) (d/squuid)}
                          tx-fragment))
         tx-result @(d/transact connection tx-data)
         record (d/entity (:db-after tx-result) (d/resolve-tempid (:db-after tx-result) (:tempids tx-result) tempid))]
     (condp = return
       :id (:db/id record)
       :entity record
       :record record))))

(defn detect-or-create
  ([self params] (detect-or-create self params {} (d/connect helpers/default-db-uri)))
  ([self params options] (detect-or-create self params options (d/connect helpers/default-db-uri)))
  ([self params {:keys [return] :or {return :record} :as options} connection]
   (let [record (if-let [entity (detect self params options (d/db connection))]
                  entity
                  (create self params options connection))]
     (condp = return
       :id (:db/id record)
       :entity record
       :record record))))

;; INSTANCE
(defn destroy
  ([self] (destroy self (d/connect helpers/default-db-uri)))
  ([self connection]
   (d/transact connection [[:db.fn/retractEntity (-> self :entity :db/id)]])))

(defn revise
  ([self params] (revise self params (d/connect helpers/default-db-uri)))
  ([self params connection]
   (let [tx-data (mapv (fn [[key value]] [:db/add (.id self) key value])
                       (reduce-kv (fn [memo key value] (assoc memo (helpers/resource-attribute (.ident self) key) value)) {} params))]
     (d/transact connection tx-data))))

(defn attr [self attribute]
  (let [attribute-manifest (-> self
                           .manifest
                           (get (.ident self))
                           (get attribute))
        default-value (-> self
                          .-entity
                          (get (helpers/resource-attribute (.ident self) attribute)))]

    (if (= :ref (:type attribute-manifest))
      (if-let [ref-type (:ref-type attribute-manifest)]
        ((resolve
          (symbol (str "spica.model." (str (name ref-type))) (str "->" (->PascalCase (str (name ref-type))) "Instance")))
         ref-type
         default-value)
        default-value)
      default-value)))

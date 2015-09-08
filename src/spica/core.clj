(ns spica.core
  (:require [spica.resource.base :as base]
            [spica.resource.helpers :as helpers]
            [spica.protocol.collection :as collection-protocol :refer (SpicaCollectionProtocol)]
            [spica.protocol.instance :as instance-protocol :refer (SpicaInstanceProtocol)]
            [spica.protocol.class :as class-protocol :refer (SpicaClassProtocol)]))

(declare ->>SpicaCollection)

(defrecord SpicaCollection [spica-class collection total]
  SpicaCollectionProtocol
  (collection-protocol/scope [self scope-name] (collection-protocol/scope self scope-name {}))
  (collection-protocol/scope [self scope-name options]
    (->>SpicaCollection
     spica-class
     (filter
      #((resolve
         (symbol (str "spica.scope." (helpers/resource-ident->resource-namespace spica-class)) (str (name scope-name))))
        %1
        options)
      collection)))

  (collection-protocol/sorter [self sorter-name]
    (->>SpicaCollection
     spica-class
     (sort
      (var-get
       (resolve
        (symbol (str "spica.sorter." (helpers/resource-ident->resource-namespace spica-class)) (str (name sorter-name)))))
      collection)))

  (collection-protocol/serialize [self serializer]
    (map #(.serialize % serializer) (:collection self))))

(defn ->>SpicaCollection [spica-class collection]
  (->SpicaCollection spica-class collection (count collection)))

(defmacro SpicaInstance [instance-name & body]
  `(defrecord ~instance-name [~'ident ~'entity]
     SpicaInstanceProtocol
     (instance-protocol/serialize [self# serializer#] ((resolve (symbol (str "spica.serializer." (helpers/resource-ident->resource-namespace ~'ident)) (str (name serializer#))))
                                                       self#))

     (instance-protocol/data [self# data-fn#] ((resolve (symbol (str "spica.data." (helpers/resource-ident->resource-namespace ~'ident)) (str (name data-fn#))))
                                               self#))

     (instance-protocol/destroy [self#] (base/destroy self#))
     (instance-protocol/destroy [self# connection#] (base/destroy self# connection#))

     (instance-protocol/revise [self# params#] (base/revise self# params#))
     (instance-protocol/revise [self# params# connection#] (base/revise self# params# connection#))

     (instance-protocol/id [self#] (:db/id (.-entity self#)))
     (instance-protocol/attr [self# attribute#] (base/attr self# attribute#))
     (instance-protocol/manifest [self#] (var-get (resolve (symbol (str "spica.model." (helpers/resource-ident->resource-namespace ~'ident)) "manifest"))))

     ~@body))

(defmacro SpicaClass [class-name manifest instance-constructor & body]
  `(defrecord ~class-name []
     SpicaClassProtocol
     (class-protocol/manifest [self#] ~manifest)
     (class-protocol/ident [self#] (base/ident self#))
     (class-protocol/schema [self#] (base/schema self#))
     (instance-protocol/data [self# data-fn#] ((resolve (symbol (str "spica.data." (helpers/resource-ident->resource-namespace (.ident self#))) (str (name data-fn#))))
                                               self#))

     (class-protocol/all [self#] (->>SpicaCollection (.ident self#) (map ~instance-constructor (base/all self#))))
     (class-protocol/all [self# options#] (->>SpicaCollection (.ident self#) (map ~instance-constructor (base/all self# options#))))
     (class-protocol/all [self# options# db#] (->>SpicaCollection (.ident self#) (map ~instance-constructor (base/all self# options# db#))))

     (class-protocol/one [self#] (~instance-constructor (base/one self#)))
     (class-protocol/one [self# options#] (~instance-constructor (base/one self# options#)))
     (class-protocol/one [self# options# db#] (~instance-constructor (base/one self# options# db#)))

     (class-protocol/where [self# params#] (->>SpicaCollection (.ident self#) (map ~instance-constructor (base/where self# params#))))
     (class-protocol/where [self# params# options#] (->>SpicaCollection (.ident self#) (map ~instance-constructor (base/where self# params# options#))))
     (class-protocol/where [self# params# options# db#] (->>SpicaCollection (.ident self#) (map ~instance-constructor (base/where self# params# options# db#))))

     (class-protocol/detect [self# params#] (if-let [record# (base/detect self# params#)] (~instance-constructor record#) nil))
     (class-protocol/detect [self# params# options#] (if-let [record# (base/detect self# params# options#)] (~instance-constructor record#) nil))
     (class-protocol/detect [self# params# options# db#] (if-let [record# (base/detect self# params# options# db#)] (~instance-constructor record#) nil))

     (class-protocol/lookup [self# id#] (if-let [record# (base/lookup self# id#)] (~instance-constructor record#) nil))
     (class-protocol/lookup [self# id# options#] (if-let [record# (base/lookup self# id# options#)] (~instance-constructor record#) nil))
     (class-protocol/lookup [self# id# options# db#] (if-let [record# (base/lookup self# id# options# db#)] (~instance-constructor record#) nil))

     (class-protocol/destroy-all [self#] (base/destroy-all! self#))
     (class-protocol/destroy-all [self# connection#] (base/destroy-all! self# connection#))

     (class-protocol/destroy-where [self# params#] (base/destroy-where! self# params#))
     (class-protocol/destroy-where [self# params# connection#] (base/destroy-where! self# params# connection#))

     (class-protocol/build [self# params#] (base/build self# params#))
     (class-protocol/build [self# params# options#] (base/build self# params# options#))
     (class-protocol/build [self# params# options# connection#] (base/build self# params# options# connection#))

     (class-protocol/create [self# params#] (~instance-constructor (base/create self# params#)))
     (class-protocol/create [self# params# options#] (~instance-constructor (base/create self# params# options#)))
     (class-protocol/create [self# params# options# connection#] (~instance-constructor (base/create self# params# options# connection#)))

     (class-protocol/detect-or-create [self# params#] (~instance-constructor (base/detect-or-create self# params#)))
     (class-protocol/detect-or-create [self# params# options#] (~instance-constructor (base/detect-or-create self# params# options#)))
     (class-protocol/detect-or-create [self# params# options# connection#] (~instance-constructor (base/detect-or-create self# params# options# connection#)))

     ~@body))

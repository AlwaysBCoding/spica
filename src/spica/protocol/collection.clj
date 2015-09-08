(ns spica.protocol.collection)

(defprotocol SpicaCollectionProtocol
  ""
  (scope [self scope-name] [self scope-name options])
  (sorter [self sorter-name])
  (serialize [self serializer]))

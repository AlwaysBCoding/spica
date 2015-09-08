(ns spica.protocol.class)

(defprotocol SpicaClassProtocol
  ""
  (manifest [self])
  (ident [self])
  (schema [self])
  (data [self data-fn])

  (all [self] [self options] [self options db])
  (one [self] [self options] [self options db])
  (where [self params] [self params options] [self params options db])
  (detect [self params] [self params options] [self params options db])
  (lookup [self id] [self id options] [self id options db])

  (destroy-all [self] [self connection])
  (destroy-where [self params] [self params connection])
  (build [self params] [self params options] [self params options connection])
  (create [self params] [self params options] [self params options connection])
  (detect-or-create [self params] [self params options] [self params options connection]))

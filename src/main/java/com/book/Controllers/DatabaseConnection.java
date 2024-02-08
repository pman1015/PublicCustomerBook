package com.book.Controllers;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class DatabaseConnection {

  String uri =
    "";
  ConnectionString connectionString = new ConnectionString(uri);
  CodecRegistry pojoCodecRegistry = fromProviders(
    PojoCodecProvider.builder().automatic(true).build()
  );
  CodecRegistry codecRegistry = fromRegistries(
    MongoClientSettings.getDefaultCodecRegistry(),
    pojoCodecRegistry
  );
  MongoClientSettings clientSettings = MongoClientSettings
    .builder()
    .applyConnectionString(connectionString)
    .codecRegistry(codecRegistry)
    .build();
  MongoClient mongoClient = MongoClients.create(clientSettings);
  MongoDatabase database = mongoClient.getDatabase("CustomerBook");
}

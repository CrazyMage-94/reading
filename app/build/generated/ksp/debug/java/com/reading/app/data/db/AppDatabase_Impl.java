package com.reading.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile BookDao _bookDao;

  private volatile SourceDao _sourceDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `books` (`bookUrl` TEXT NOT NULL, `name` TEXT NOT NULL, `author` TEXT NOT NULL, `coverUrl` TEXT NOT NULL, `intro` TEXT NOT NULL, `sourceId` TEXT NOT NULL, `totalChapters` INTEGER NOT NULL, `lastReadIndex` INTEGER NOT NULL, `lastReadOffset` INTEGER NOT NULL, `addedAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, PRIMARY KEY(`bookUrl`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `chapters` (`bookUrl` TEXT NOT NULL, `index` INTEGER NOT NULL, `title` TEXT NOT NULL, `url` TEXT NOT NULL, `isCached` INTEGER NOT NULL, PRIMARY KEY(`bookUrl`, `index`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `book_sources` (`id` TEXT NOT NULL, `name` TEXT NOT NULL, `searchUrl` TEXT NOT NULL, `charset` TEXT NOT NULL, `ruleSearchBookList` TEXT NOT NULL, `ruleSearchName` TEXT NOT NULL, `ruleSearchAuthor` TEXT NOT NULL, `ruleSearchCover` TEXT NOT NULL, `ruleSearchBookUrl` TEXT NOT NULL, `ruleBookIntro` TEXT NOT NULL, `ruleBookCover` TEXT NOT NULL, `ruleChapterList` TEXT NOT NULL, `ruleChapterName` TEXT NOT NULL, `ruleChapterUrl` TEXT NOT NULL, `ruleContent` TEXT NOT NULL, `enabled` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'e0fb4488079e74b9e2ad9f4afefa0ebc')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `books`");
        db.execSQL("DROP TABLE IF EXISTS `chapters`");
        db.execSQL("DROP TABLE IF EXISTS `book_sources`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsBooks = new HashMap<String, TableInfo.Column>(11);
        _columnsBooks.put("bookUrl", new TableInfo.Column("bookUrl", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("author", new TableInfo.Column("author", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("coverUrl", new TableInfo.Column("coverUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("intro", new TableInfo.Column("intro", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("sourceId", new TableInfo.Column("sourceId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("totalChapters", new TableInfo.Column("totalChapters", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("lastReadIndex", new TableInfo.Column("lastReadIndex", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("lastReadOffset", new TableInfo.Column("lastReadOffset", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("addedAt", new TableInfo.Column("addedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBooks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBooks = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBooks = new TableInfo("books", _columnsBooks, _foreignKeysBooks, _indicesBooks);
        final TableInfo _existingBooks = TableInfo.read(db, "books");
        if (!_infoBooks.equals(_existingBooks)) {
          return new RoomOpenHelper.ValidationResult(false, "books(com.reading.app.data.model.Book).\n"
                  + " Expected:\n" + _infoBooks + "\n"
                  + " Found:\n" + _existingBooks);
        }
        final HashMap<String, TableInfo.Column> _columnsChapters = new HashMap<String, TableInfo.Column>(5);
        _columnsChapters.put("bookUrl", new TableInfo.Column("bookUrl", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapters.put("index", new TableInfo.Column("index", "INTEGER", true, 2, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapters.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapters.put("url", new TableInfo.Column("url", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsChapters.put("isCached", new TableInfo.Column("isCached", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysChapters = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesChapters = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoChapters = new TableInfo("chapters", _columnsChapters, _foreignKeysChapters, _indicesChapters);
        final TableInfo _existingChapters = TableInfo.read(db, "chapters");
        if (!_infoChapters.equals(_existingChapters)) {
          return new RoomOpenHelper.ValidationResult(false, "chapters(com.reading.app.data.model.Chapter).\n"
                  + " Expected:\n" + _infoChapters + "\n"
                  + " Found:\n" + _existingChapters);
        }
        final HashMap<String, TableInfo.Column> _columnsBookSources = new HashMap<String, TableInfo.Column>(16);
        _columnsBookSources.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookSources.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookSources.put("searchUrl", new TableInfo.Column("searchUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookSources.put("charset", new TableInfo.Column("charset", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookSources.put("ruleSearchBookList", new TableInfo.Column("ruleSearchBookList", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookSources.put("ruleSearchName", new TableInfo.Column("ruleSearchName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookSources.put("ruleSearchAuthor", new TableInfo.Column("ruleSearchAuthor", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookSources.put("ruleSearchCover", new TableInfo.Column("ruleSearchCover", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookSources.put("ruleSearchBookUrl", new TableInfo.Column("ruleSearchBookUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookSources.put("ruleBookIntro", new TableInfo.Column("ruleBookIntro", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookSources.put("ruleBookCover", new TableInfo.Column("ruleBookCover", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookSources.put("ruleChapterList", new TableInfo.Column("ruleChapterList", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookSources.put("ruleChapterName", new TableInfo.Column("ruleChapterName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookSources.put("ruleChapterUrl", new TableInfo.Column("ruleChapterUrl", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookSources.put("ruleContent", new TableInfo.Column("ruleContent", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBookSources.put("enabled", new TableInfo.Column("enabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBookSources = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBookSources = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBookSources = new TableInfo("book_sources", _columnsBookSources, _foreignKeysBookSources, _indicesBookSources);
        final TableInfo _existingBookSources = TableInfo.read(db, "book_sources");
        if (!_infoBookSources.equals(_existingBookSources)) {
          return new RoomOpenHelper.ValidationResult(false, "book_sources(com.reading.app.data.model.BookSource).\n"
                  + " Expected:\n" + _infoBookSources + "\n"
                  + " Found:\n" + _existingBookSources);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "e0fb4488079e74b9e2ad9f4afefa0ebc", "b0d5357031d4d6c18551a5d724796121");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "books","chapters","book_sources");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `books`");
      _db.execSQL("DELETE FROM `chapters`");
      _db.execSQL("DELETE FROM `book_sources`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(BookDao.class, BookDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SourceDao.class, SourceDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public BookDao bookDao() {
    if (_bookDao != null) {
      return _bookDao;
    } else {
      synchronized(this) {
        if(_bookDao == null) {
          _bookDao = new BookDao_Impl(this);
        }
        return _bookDao;
      }
    }
  }

  @Override
  public SourceDao sourceDao() {
    if (_sourceDao != null) {
      return _sourceDao;
    } else {
      synchronized(this) {
        if(_sourceDao == null) {
          _sourceDao = new SourceDao_Impl(this);
        }
        return _sourceDao;
      }
    }
  }
}

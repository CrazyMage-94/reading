package com.reading.app.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.reading.app.data.model.Book;
import com.reading.app.data.model.Chapter;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BookDao_Impl implements BookDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Book> __insertionAdapterOfBook;

  private final EntityInsertionAdapter<Chapter> __insertionAdapterOfChapter;

  private final EntityDeletionOrUpdateAdapter<Book> __deletionAdapterOfBook;

  private final SharedSQLiteStatement __preparedStmtOfUpdateProgress;

  private final SharedSQLiteStatement __preparedStmtOfMarkCached;

  public BookDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBook = new EntityInsertionAdapter<Book>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `books` (`bookUrl`,`name`,`author`,`coverUrl`,`intro`,`sourceId`,`totalChapters`,`lastReadIndex`,`lastReadOffset`,`addedAt`,`updatedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Book entity) {
        statement.bindString(1, entity.getBookUrl());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getAuthor());
        statement.bindString(4, entity.getCoverUrl());
        statement.bindString(5, entity.getIntro());
        statement.bindString(6, entity.getSourceId());
        statement.bindLong(7, entity.getTotalChapters());
        statement.bindLong(8, entity.getLastReadIndex());
        statement.bindLong(9, entity.getLastReadOffset());
        statement.bindLong(10, entity.getAddedAt());
        statement.bindLong(11, entity.getUpdatedAt());
      }
    };
    this.__insertionAdapterOfChapter = new EntityInsertionAdapter<Chapter>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `chapters` (`bookUrl`,`index`,`title`,`url`,`isCached`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Chapter entity) {
        statement.bindString(1, entity.getBookUrl());
        statement.bindLong(2, entity.getIndex());
        statement.bindString(3, entity.getTitle());
        statement.bindString(4, entity.getUrl());
        final int _tmp = entity.isCached() ? 1 : 0;
        statement.bindLong(5, _tmp);
      }
    };
    this.__deletionAdapterOfBook = new EntityDeletionOrUpdateAdapter<Book>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `books` WHERE `bookUrl` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Book entity) {
        statement.bindString(1, entity.getBookUrl());
      }
    };
    this.__preparedStmtOfUpdateProgress = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE books SET lastReadIndex = ?, updatedAt = ? WHERE bookUrl = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkCached = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE chapters SET isCached = 1 WHERE bookUrl = ? AND `index` = ?";
        return _query;
      }
    };
  }

  @Override
  public Object upsert(final Book book, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBook.insert(book);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object upsertChapters(final List<Chapter> chapters,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfChapter.insert(chapters);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final Book book, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfBook.handle(book);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateProgress(final String url, final int index, final long time,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateProgress.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, index);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, time);
        _argIndex = 3;
        _stmt.bindString(_argIndex, url);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateProgress.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markCached(final String url, final int idx,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkCached.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, url);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, idx);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkCached.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Book>> getAllBooks() {
    final String _sql = "SELECT * FROM books ORDER BY updatedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"books"}, new Callable<List<Book>>() {
      @Override
      @NonNull
      public List<Book> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBookUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "bookUrl");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfCoverUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "coverUrl");
          final int _cursorIndexOfIntro = CursorUtil.getColumnIndexOrThrow(_cursor, "intro");
          final int _cursorIndexOfSourceId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceId");
          final int _cursorIndexOfTotalChapters = CursorUtil.getColumnIndexOrThrow(_cursor, "totalChapters");
          final int _cursorIndexOfLastReadIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadIndex");
          final int _cursorIndexOfLastReadOffset = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadOffset");
          final int _cursorIndexOfAddedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "addedAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<Book> _result = new ArrayList<Book>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Book _item;
            final String _tmpBookUrl;
            _tmpBookUrl = _cursor.getString(_cursorIndexOfBookUrl);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final String _tmpCoverUrl;
            _tmpCoverUrl = _cursor.getString(_cursorIndexOfCoverUrl);
            final String _tmpIntro;
            _tmpIntro = _cursor.getString(_cursorIndexOfIntro);
            final String _tmpSourceId;
            _tmpSourceId = _cursor.getString(_cursorIndexOfSourceId);
            final int _tmpTotalChapters;
            _tmpTotalChapters = _cursor.getInt(_cursorIndexOfTotalChapters);
            final int _tmpLastReadIndex;
            _tmpLastReadIndex = _cursor.getInt(_cursorIndexOfLastReadIndex);
            final int _tmpLastReadOffset;
            _tmpLastReadOffset = _cursor.getInt(_cursorIndexOfLastReadOffset);
            final long _tmpAddedAt;
            _tmpAddedAt = _cursor.getLong(_cursorIndexOfAddedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new Book(_tmpBookUrl,_tmpName,_tmpAuthor,_tmpCoverUrl,_tmpIntro,_tmpSourceId,_tmpTotalChapters,_tmpLastReadIndex,_tmpLastReadOffset,_tmpAddedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getBook(final String url, final Continuation<? super Book> $completion) {
    final String _sql = "SELECT * FROM books WHERE bookUrl = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, url);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Book>() {
      @Override
      @Nullable
      public Book call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBookUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "bookUrl");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfCoverUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "coverUrl");
          final int _cursorIndexOfIntro = CursorUtil.getColumnIndexOrThrow(_cursor, "intro");
          final int _cursorIndexOfSourceId = CursorUtil.getColumnIndexOrThrow(_cursor, "sourceId");
          final int _cursorIndexOfTotalChapters = CursorUtil.getColumnIndexOrThrow(_cursor, "totalChapters");
          final int _cursorIndexOfLastReadIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadIndex");
          final int _cursorIndexOfLastReadOffset = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadOffset");
          final int _cursorIndexOfAddedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "addedAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final Book _result;
          if (_cursor.moveToFirst()) {
            final String _tmpBookUrl;
            _tmpBookUrl = _cursor.getString(_cursorIndexOfBookUrl);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            final String _tmpCoverUrl;
            _tmpCoverUrl = _cursor.getString(_cursorIndexOfCoverUrl);
            final String _tmpIntro;
            _tmpIntro = _cursor.getString(_cursorIndexOfIntro);
            final String _tmpSourceId;
            _tmpSourceId = _cursor.getString(_cursorIndexOfSourceId);
            final int _tmpTotalChapters;
            _tmpTotalChapters = _cursor.getInt(_cursorIndexOfTotalChapters);
            final int _tmpLastReadIndex;
            _tmpLastReadIndex = _cursor.getInt(_cursorIndexOfLastReadIndex);
            final int _tmpLastReadOffset;
            _tmpLastReadOffset = _cursor.getInt(_cursorIndexOfLastReadOffset);
            final long _tmpAddedAt;
            _tmpAddedAt = _cursor.getLong(_cursorIndexOfAddedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new Book(_tmpBookUrl,_tmpName,_tmpAuthor,_tmpCoverUrl,_tmpIntro,_tmpSourceId,_tmpTotalChapters,_tmpLastReadIndex,_tmpLastReadOffset,_tmpAddedAt,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getChapters(final String url,
      final Continuation<? super List<Chapter>> $completion) {
    final String _sql = "SELECT * FROM chapters WHERE bookUrl = ? ORDER BY `index` ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, url);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Chapter>>() {
      @Override
      @NonNull
      public List<Chapter> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfBookUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "bookUrl");
          final int _cursorIndexOfIndex = CursorUtil.getColumnIndexOrThrow(_cursor, "index");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
          final int _cursorIndexOfIsCached = CursorUtil.getColumnIndexOrThrow(_cursor, "isCached");
          final List<Chapter> _result = new ArrayList<Chapter>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Chapter _item;
            final String _tmpBookUrl;
            _tmpBookUrl = _cursor.getString(_cursorIndexOfBookUrl);
            final int _tmpIndex;
            _tmpIndex = _cursor.getInt(_cursorIndexOfIndex);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpUrl;
            _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
            final boolean _tmpIsCached;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCached);
            _tmpIsCached = _tmp != 0;
            _item = new Chapter(_tmpBookUrl,_tmpIndex,_tmpTitle,_tmpUrl,_tmpIsCached);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}

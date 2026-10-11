package com.reading.app.data.db;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.reading.app.data.model.BookSource;
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
public final class SourceDao_Impl implements SourceDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BookSource> __insertionAdapterOfBookSource;

  private final SharedSQLiteStatement __preparedStmtOfSetEnabled;

  public SourceDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBookSource = new EntityInsertionAdapter<BookSource>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `book_sources` (`id`,`name`,`searchUrl`,`charset`,`ruleSearchBookList`,`ruleSearchName`,`ruleSearchAuthor`,`ruleSearchCover`,`ruleSearchBookUrl`,`ruleBookIntro`,`ruleBookCover`,`ruleChapterList`,`ruleChapterName`,`ruleChapterUrl`,`ruleContent`,`enabled`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BookSource entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getSearchUrl());
        statement.bindString(4, entity.getCharset());
        statement.bindString(5, entity.getRuleSearchBookList());
        statement.bindString(6, entity.getRuleSearchName());
        statement.bindString(7, entity.getRuleSearchAuthor());
        statement.bindString(8, entity.getRuleSearchCover());
        statement.bindString(9, entity.getRuleSearchBookUrl());
        statement.bindString(10, entity.getRuleBookIntro());
        statement.bindString(11, entity.getRuleBookCover());
        statement.bindString(12, entity.getRuleChapterList());
        statement.bindString(13, entity.getRuleChapterName());
        statement.bindString(14, entity.getRuleChapterUrl());
        statement.bindString(15, entity.getRuleContent());
        final int _tmp = entity.getEnabled() ? 1 : 0;
        statement.bindLong(16, _tmp);
      }
    };
    this.__preparedStmtOfSetEnabled = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE book_sources SET enabled = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object upsertAll(final List<BookSource> sources,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBookSource.insert(sources);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object setEnabled(final String id, final boolean enabled,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetEnabled.acquire();
        int _argIndex = 1;
        final int _tmp = enabled ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, id);
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
          __preparedStmtOfSetEnabled.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<BookSource>> getEnabledSources() {
    final String _sql = "SELECT * FROM book_sources WHERE enabled = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"book_sources"}, new Callable<List<BookSource>>() {
      @Override
      @NonNull
      public List<BookSource> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSearchUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "searchUrl");
          final int _cursorIndexOfCharset = CursorUtil.getColumnIndexOrThrow(_cursor, "charset");
          final int _cursorIndexOfRuleSearchBookList = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleSearchBookList");
          final int _cursorIndexOfRuleSearchName = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleSearchName");
          final int _cursorIndexOfRuleSearchAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleSearchAuthor");
          final int _cursorIndexOfRuleSearchCover = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleSearchCover");
          final int _cursorIndexOfRuleSearchBookUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleSearchBookUrl");
          final int _cursorIndexOfRuleBookIntro = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleBookIntro");
          final int _cursorIndexOfRuleBookCover = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleBookCover");
          final int _cursorIndexOfRuleChapterList = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleChapterList");
          final int _cursorIndexOfRuleChapterName = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleChapterName");
          final int _cursorIndexOfRuleChapterUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleChapterUrl");
          final int _cursorIndexOfRuleContent = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleContent");
          final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
          final List<BookSource> _result = new ArrayList<BookSource>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BookSource _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpSearchUrl;
            _tmpSearchUrl = _cursor.getString(_cursorIndexOfSearchUrl);
            final String _tmpCharset;
            _tmpCharset = _cursor.getString(_cursorIndexOfCharset);
            final String _tmpRuleSearchBookList;
            _tmpRuleSearchBookList = _cursor.getString(_cursorIndexOfRuleSearchBookList);
            final String _tmpRuleSearchName;
            _tmpRuleSearchName = _cursor.getString(_cursorIndexOfRuleSearchName);
            final String _tmpRuleSearchAuthor;
            _tmpRuleSearchAuthor = _cursor.getString(_cursorIndexOfRuleSearchAuthor);
            final String _tmpRuleSearchCover;
            _tmpRuleSearchCover = _cursor.getString(_cursorIndexOfRuleSearchCover);
            final String _tmpRuleSearchBookUrl;
            _tmpRuleSearchBookUrl = _cursor.getString(_cursorIndexOfRuleSearchBookUrl);
            final String _tmpRuleBookIntro;
            _tmpRuleBookIntro = _cursor.getString(_cursorIndexOfRuleBookIntro);
            final String _tmpRuleBookCover;
            _tmpRuleBookCover = _cursor.getString(_cursorIndexOfRuleBookCover);
            final String _tmpRuleChapterList;
            _tmpRuleChapterList = _cursor.getString(_cursorIndexOfRuleChapterList);
            final String _tmpRuleChapterName;
            _tmpRuleChapterName = _cursor.getString(_cursorIndexOfRuleChapterName);
            final String _tmpRuleChapterUrl;
            _tmpRuleChapterUrl = _cursor.getString(_cursorIndexOfRuleChapterUrl);
            final String _tmpRuleContent;
            _tmpRuleContent = _cursor.getString(_cursorIndexOfRuleContent);
            final boolean _tmpEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfEnabled);
            _tmpEnabled = _tmp != 0;
            _item = new BookSource(_tmpId,_tmpName,_tmpSearchUrl,_tmpCharset,_tmpRuleSearchBookList,_tmpRuleSearchName,_tmpRuleSearchAuthor,_tmpRuleSearchCover,_tmpRuleSearchBookUrl,_tmpRuleBookIntro,_tmpRuleBookCover,_tmpRuleChapterList,_tmpRuleChapterName,_tmpRuleChapterUrl,_tmpRuleContent,_tmpEnabled);
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
  public Object getEnabledSourcesOnce(final Continuation<? super List<BookSource>> $completion) {
    final String _sql = "SELECT * FROM book_sources WHERE enabled = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<BookSource>>() {
      @Override
      @NonNull
      public List<BookSource> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfSearchUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "searchUrl");
          final int _cursorIndexOfCharset = CursorUtil.getColumnIndexOrThrow(_cursor, "charset");
          final int _cursorIndexOfRuleSearchBookList = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleSearchBookList");
          final int _cursorIndexOfRuleSearchName = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleSearchName");
          final int _cursorIndexOfRuleSearchAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleSearchAuthor");
          final int _cursorIndexOfRuleSearchCover = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleSearchCover");
          final int _cursorIndexOfRuleSearchBookUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleSearchBookUrl");
          final int _cursorIndexOfRuleBookIntro = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleBookIntro");
          final int _cursorIndexOfRuleBookCover = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleBookCover");
          final int _cursorIndexOfRuleChapterList = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleChapterList");
          final int _cursorIndexOfRuleChapterName = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleChapterName");
          final int _cursorIndexOfRuleChapterUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleChapterUrl");
          final int _cursorIndexOfRuleContent = CursorUtil.getColumnIndexOrThrow(_cursor, "ruleContent");
          final int _cursorIndexOfEnabled = CursorUtil.getColumnIndexOrThrow(_cursor, "enabled");
          final List<BookSource> _result = new ArrayList<BookSource>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BookSource _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpSearchUrl;
            _tmpSearchUrl = _cursor.getString(_cursorIndexOfSearchUrl);
            final String _tmpCharset;
            _tmpCharset = _cursor.getString(_cursorIndexOfCharset);
            final String _tmpRuleSearchBookList;
            _tmpRuleSearchBookList = _cursor.getString(_cursorIndexOfRuleSearchBookList);
            final String _tmpRuleSearchName;
            _tmpRuleSearchName = _cursor.getString(_cursorIndexOfRuleSearchName);
            final String _tmpRuleSearchAuthor;
            _tmpRuleSearchAuthor = _cursor.getString(_cursorIndexOfRuleSearchAuthor);
            final String _tmpRuleSearchCover;
            _tmpRuleSearchCover = _cursor.getString(_cursorIndexOfRuleSearchCover);
            final String _tmpRuleSearchBookUrl;
            _tmpRuleSearchBookUrl = _cursor.getString(_cursorIndexOfRuleSearchBookUrl);
            final String _tmpRuleBookIntro;
            _tmpRuleBookIntro = _cursor.getString(_cursorIndexOfRuleBookIntro);
            final String _tmpRuleBookCover;
            _tmpRuleBookCover = _cursor.getString(_cursorIndexOfRuleBookCover);
            final String _tmpRuleChapterList;
            _tmpRuleChapterList = _cursor.getString(_cursorIndexOfRuleChapterList);
            final String _tmpRuleChapterName;
            _tmpRuleChapterName = _cursor.getString(_cursorIndexOfRuleChapterName);
            final String _tmpRuleChapterUrl;
            _tmpRuleChapterUrl = _cursor.getString(_cursorIndexOfRuleChapterUrl);
            final String _tmpRuleContent;
            _tmpRuleContent = _cursor.getString(_cursorIndexOfRuleContent);
            final boolean _tmpEnabled;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfEnabled);
            _tmpEnabled = _tmp != 0;
            _item = new BookSource(_tmpId,_tmpName,_tmpSearchUrl,_tmpCharset,_tmpRuleSearchBookList,_tmpRuleSearchName,_tmpRuleSearchAuthor,_tmpRuleSearchCover,_tmpRuleSearchBookUrl,_tmpRuleBookIntro,_tmpRuleBookCover,_tmpRuleChapterList,_tmpRuleChapterName,_tmpRuleChapterUrl,_tmpRuleContent,_tmpEnabled);
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

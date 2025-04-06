
// Description: Java 11 in-memory RAM DbIO implementation for IndexCol.

/*
 *	org.msscf.msscf.CFBam
 *
 *	Copyright (c) 2020 Mark Stephen Sobkow
 *	
 *	MSS Code Factory CFBam 2.13 Business Application Model
 *	
 *	Copyright 2020-2021 Mark Stephen Sobkow
 *	
 *		This file is part of MSS Code Factory.
 *	
 *		MSS Code Factory is available under dual commercial license from Mark Stephen
 *		Sobkow, or under the terms of the GNU General Public License, Version 3
 *		or later.
 *	
 *	    MSS Code Factory is free software: you can redistribute it and/or modify
 *	    it under the terms of the GNU General Public License as published by
 *	    the Free Software Foundation, either version 3 of the License, or
 *	    (at your option) any later version.
 *	
 *	    MSS Code Factory is distributed in the hope that it will be useful,
 *	    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	    GNU General Public License for more details.
 *	
 *	    You should have received a copy of the GNU General Public License
 *	    along with MSS Code Factory.  If not, see <https://www.gnu.org/licenses/>.
 *	
 *	Donations to support MSS Code Factory can be made at
 *	https://www.paypal.com/paypalme2/MarkSobkow
 *	
 *	Please contact Mark Stephen Sobkow at mark.sobkow@gmail.com for commercial licensing.
 *
 *	Manufactured by MSS Code Factory 2.12
 */

package org.msscf.msscf.v2_13.cfbam.CFBamRam;

import java.math.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import org.apache.commons.codec.binary.Base64;
import org.msscf.msscf.v2_13.cflib.CFLib.*;
import org.msscf.msscf.v2_13.cfsec.CFSec.*;
import org.msscf.msscf.v2_13.cfint.CFInt.*;
import org.msscf.msscf.v2_13.cfbam.CFBam.*;
import org.msscf.msscf.v2_13.cfbam.CFBamObj.*;
import org.msscf.msscf.v2_13.cfsec.CFSecObj.*;
import org.msscf.msscf.v2_13.cfint.CFIntObj.*;
import org.msscf.msscf.v2_13.cfbam.CFBamObj.*;

/*
 *	CFBamRamIndexColTable in-memory RAM DbIO implementation
 *	for IndexCol.
 */
public class CFBamRamIndexColTable
	implements ICFBamIndexColTable
{
	private ICFBamSchema schema;
	private Map< CFBamIndexColPKey,
				CFBamIndexColBuff > dictByPKey
		= new HashMap< CFBamIndexColPKey,
				CFBamIndexColBuff >();
	private Map< CFBamIndexColByUNameIdxKey,
			CFBamIndexColBuff > dictByUNameIdx
		= new HashMap< CFBamIndexColByUNameIdxKey,
			CFBamIndexColBuff >();
	private Map< CFBamIndexColByIdxColTenantIdxKey,
				Map< CFBamIndexColPKey,
					CFBamIndexColBuff >> dictByIdxColTenantIdx
		= new HashMap< CFBamIndexColByIdxColTenantIdxKey,
				Map< CFBamIndexColPKey,
					CFBamIndexColBuff >>();
	private Map< CFBamIndexColByIndexIdxKey,
				Map< CFBamIndexColPKey,
					CFBamIndexColBuff >> dictByIndexIdx
		= new HashMap< CFBamIndexColByIndexIdxKey,
				Map< CFBamIndexColPKey,
					CFBamIndexColBuff >>();
	private Map< CFBamIndexColByDefSchemaIdxKey,
				Map< CFBamIndexColPKey,
					CFBamIndexColBuff >> dictByDefSchemaIdx
		= new HashMap< CFBamIndexColByDefSchemaIdxKey,
				Map< CFBamIndexColPKey,
					CFBamIndexColBuff >>();
	private Map< CFBamIndexColByColIdxKey,
				Map< CFBamIndexColPKey,
					CFBamIndexColBuff >> dictByColIdx
		= new HashMap< CFBamIndexColByColIdxKey,
				Map< CFBamIndexColPKey,
					CFBamIndexColBuff >>();
	private Map< CFBamIndexColByPrevIdxKey,
				Map< CFBamIndexColPKey,
					CFBamIndexColBuff >> dictByPrevIdx
		= new HashMap< CFBamIndexColByPrevIdxKey,
				Map< CFBamIndexColPKey,
					CFBamIndexColBuff >>();
	private Map< CFBamIndexColByNextIdxKey,
				Map< CFBamIndexColPKey,
					CFBamIndexColBuff >> dictByNextIdx
		= new HashMap< CFBamIndexColByNextIdxKey,
				Map< CFBamIndexColPKey,
					CFBamIndexColBuff >>();
	private Map< CFBamIndexColByIdxPrevIdxKey,
				Map< CFBamIndexColPKey,
					CFBamIndexColBuff >> dictByIdxPrevIdx
		= new HashMap< CFBamIndexColByIdxPrevIdxKey,
				Map< CFBamIndexColPKey,
					CFBamIndexColBuff >>();
	private Map< CFBamIndexColByIdxNextIdxKey,
				Map< CFBamIndexColPKey,
					CFBamIndexColBuff >> dictByIdxNextIdx
		= new HashMap< CFBamIndexColByIdxNextIdxKey,
				Map< CFBamIndexColPKey,
					CFBamIndexColBuff >>();

	public CFBamRamIndexColTable( ICFBamSchema argSchema ) {
		schema = argSchema;
	}

	public void createIndexCol( CFSecAuthorization Authorization,
		CFBamIndexColBuff Buff )
	{
		final String S_ProcName = "createIndexCol";
			CFBamIndexColBuff tail = null;

			CFBamIndexColBuff[] siblings = schema.getTableIndexCol().readDerivedByIndexIdx( Authorization,
				Buff.getRequiredTenantId(),
				Buff.getRequiredIndexId() );
			for( int idx = 0; ( tail == null ) && ( idx < siblings.length ); idx ++ ) {
				if( ( siblings[idx].getOptionalNextTenantId() == null )
					&& ( siblings[idx].getOptionalNextId() == null ) )
				{
					tail = siblings[idx];
				}
			}
			if( tail != null ) {
				Buff.setOptionalPrevTenantId( tail.getRequiredTenantId() );
				Buff.setOptionalPrevId( tail.getRequiredId() );
			}
			else {
				Buff.setOptionalPrevTenantId( null );
				Buff.setOptionalPrevId( null );
			}
		
		CFBamIndexColPKey pkey = schema.getFactoryIndexCol().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( ((CFBamRamTenantTable)schema.getTableTenant()).nextIndexColIdGen( Authorization,
			Buff.getRequiredTenantId() ) );
		Buff.setRequiredTenantId( pkey.getRequiredTenantId() );
		Buff.setRequiredId( pkey.getRequiredId() );
		CFBamIndexColByUNameIdxKey keyUNameIdx = schema.getFactoryIndexCol().newUNameIdxKey();
		keyUNameIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keyUNameIdx.setRequiredIndexId( Buff.getRequiredIndexId() );
		keyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamIndexColByIdxColTenantIdxKey keyIdxColTenantIdx = schema.getFactoryIndexCol().newIdxColTenantIdxKey();
		keyIdxColTenantIdx.setRequiredTenantId( Buff.getRequiredTenantId() );

		CFBamIndexColByIndexIdxKey keyIndexIdx = schema.getFactoryIndexCol().newIndexIdxKey();
		keyIndexIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keyIndexIdx.setRequiredIndexId( Buff.getRequiredIndexId() );

		CFBamIndexColByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryIndexCol().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaTenantId( Buff.getOptionalDefSchemaTenantId() );
		keyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamIndexColByColIdxKey keyColIdx = schema.getFactoryIndexCol().newColIdxKey();
		keyColIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keyColIdx.setRequiredColumnId( Buff.getRequiredColumnId() );

		CFBamIndexColByPrevIdxKey keyPrevIdx = schema.getFactoryIndexCol().newPrevIdxKey();
		keyPrevIdx.setOptionalPrevTenantId( Buff.getOptionalPrevTenantId() );
		keyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamIndexColByNextIdxKey keyNextIdx = schema.getFactoryIndexCol().newNextIdxKey();
		keyNextIdx.setOptionalNextTenantId( Buff.getOptionalNextTenantId() );
		keyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		CFBamIndexColByIdxPrevIdxKey keyIdxPrevIdx = schema.getFactoryIndexCol().newIdxPrevIdxKey();
		keyIdxPrevIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keyIdxPrevIdx.setRequiredIndexId( Buff.getRequiredIndexId() );
		keyIdxPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamIndexColByIdxNextIdxKey keyIdxNextIdx = schema.getFactoryIndexCol().newIdxNextIdxKey();
		keyIdxNextIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		keyIdxNextIdx.setRequiredIndexId( Buff.getRequiredIndexId() );
		keyIdxNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Validate unique indexes

		if( dictByPKey.containsKey( pkey ) ) {
			throw new CFLibPrimaryKeyNotNewException( getClass(), S_ProcName, pkey );
		}

		if( dictByUNameIdx.containsKey( keyUNameIdx ) ) {
			throw new CFLibUniqueIndexViolationException( getClass(),
				S_ProcName,
				"IndexColUNameIdx",
				keyUNameIdx );
		}

		// Validate foreign keys

		{
			boolean allNull = true;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableTenant().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Owner",
						"Tenant",
						"Tenant",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableIndex().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredIndexId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Container",
						"Index",
						"Index",
						null );
				}
			}
		}

		{
			boolean allNull = true;
			allNull = false;
			allNull = false;
			if( ! allNull ) {
				if( null == schema.getTableValue().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredColumnId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						S_ProcName,
						"Lookup",
						"Column",
						"Value",
						null );
				}
			}
		}

		// Proceed with adding the new record

		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.put( keyUNameIdx, Buff );

		Map< CFBamIndexColPKey, CFBamIndexColBuff > subdictIdxColTenantIdx;
		if( dictByIdxColTenantIdx.containsKey( keyIdxColTenantIdx ) ) {
			subdictIdxColTenantIdx = dictByIdxColTenantIdx.get( keyIdxColTenantIdx );
		}
		else {
			subdictIdxColTenantIdx = new HashMap< CFBamIndexColPKey, CFBamIndexColBuff >();
			dictByIdxColTenantIdx.put( keyIdxColTenantIdx, subdictIdxColTenantIdx );
		}
		subdictIdxColTenantIdx.put( pkey, Buff );

		Map< CFBamIndexColPKey, CFBamIndexColBuff > subdictIndexIdx;
		if( dictByIndexIdx.containsKey( keyIndexIdx ) ) {
			subdictIndexIdx = dictByIndexIdx.get( keyIndexIdx );
		}
		else {
			subdictIndexIdx = new HashMap< CFBamIndexColPKey, CFBamIndexColBuff >();
			dictByIndexIdx.put( keyIndexIdx, subdictIndexIdx );
		}
		subdictIndexIdx.put( pkey, Buff );

		Map< CFBamIndexColPKey, CFBamIndexColBuff > subdictDefSchemaIdx;
		if( dictByDefSchemaIdx.containsKey( keyDefSchemaIdx ) ) {
			subdictDefSchemaIdx = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		}
		else {
			subdictDefSchemaIdx = new HashMap< CFBamIndexColPKey, CFBamIndexColBuff >();
			dictByDefSchemaIdx.put( keyDefSchemaIdx, subdictDefSchemaIdx );
		}
		subdictDefSchemaIdx.put( pkey, Buff );

		Map< CFBamIndexColPKey, CFBamIndexColBuff > subdictColIdx;
		if( dictByColIdx.containsKey( keyColIdx ) ) {
			subdictColIdx = dictByColIdx.get( keyColIdx );
		}
		else {
			subdictColIdx = new HashMap< CFBamIndexColPKey, CFBamIndexColBuff >();
			dictByColIdx.put( keyColIdx, subdictColIdx );
		}
		subdictColIdx.put( pkey, Buff );

		Map< CFBamIndexColPKey, CFBamIndexColBuff > subdictPrevIdx;
		if( dictByPrevIdx.containsKey( keyPrevIdx ) ) {
			subdictPrevIdx = dictByPrevIdx.get( keyPrevIdx );
		}
		else {
			subdictPrevIdx = new HashMap< CFBamIndexColPKey, CFBamIndexColBuff >();
			dictByPrevIdx.put( keyPrevIdx, subdictPrevIdx );
		}
		subdictPrevIdx.put( pkey, Buff );

		Map< CFBamIndexColPKey, CFBamIndexColBuff > subdictNextIdx;
		if( dictByNextIdx.containsKey( keyNextIdx ) ) {
			subdictNextIdx = dictByNextIdx.get( keyNextIdx );
		}
		else {
			subdictNextIdx = new HashMap< CFBamIndexColPKey, CFBamIndexColBuff >();
			dictByNextIdx.put( keyNextIdx, subdictNextIdx );
		}
		subdictNextIdx.put( pkey, Buff );

		Map< CFBamIndexColPKey, CFBamIndexColBuff > subdictIdxPrevIdx;
		if( dictByIdxPrevIdx.containsKey( keyIdxPrevIdx ) ) {
			subdictIdxPrevIdx = dictByIdxPrevIdx.get( keyIdxPrevIdx );
		}
		else {
			subdictIdxPrevIdx = new HashMap< CFBamIndexColPKey, CFBamIndexColBuff >();
			dictByIdxPrevIdx.put( keyIdxPrevIdx, subdictIdxPrevIdx );
		}
		subdictIdxPrevIdx.put( pkey, Buff );

		Map< CFBamIndexColPKey, CFBamIndexColBuff > subdictIdxNextIdx;
		if( dictByIdxNextIdx.containsKey( keyIdxNextIdx ) ) {
			subdictIdxNextIdx = dictByIdxNextIdx.get( keyIdxNextIdx );
		}
		else {
			subdictIdxNextIdx = new HashMap< CFBamIndexColPKey, CFBamIndexColBuff >();
			dictByIdxNextIdx.put( keyIdxNextIdx, subdictIdxNextIdx );
		}
		subdictIdxNextIdx.put( pkey, Buff );

		if( tail != null ) {
			CFBamIndexColBuff tailEdit = schema.getFactoryIndexCol().newBuff();
			tailEdit.set( (CFBamIndexColBuff)tail );
				tailEdit.setOptionalNextTenantId( Buff.getRequiredTenantId() );
				tailEdit.setOptionalNextId( Buff.getRequiredId() );
			schema.getTableIndexCol().updateIndexCol( Authorization, tailEdit );
		}
	}

	public CFBamIndexColBuff readDerived( CFSecAuthorization Authorization,
		CFBamIndexColPKey PKey )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerived";
		CFBamIndexColPKey key = schema.getFactoryIndexCol().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamIndexColBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamIndexColBuff lockDerived( CFSecAuthorization Authorization,
		CFBamIndexColPKey PKey )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerived";
		CFBamIndexColPKey key = schema.getFactoryIndexCol().newPKey();
		key.setRequiredTenantId( PKey.getRequiredTenantId() );
		key.setRequiredId( PKey.getRequiredId() );
		CFBamIndexColBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamIndexColBuff[] readAllDerived( CFSecAuthorization Authorization ) {
		final String S_ProcName = "CFBamRamIndexCol.readAllDerived";
		CFBamIndexColBuff[] retList = new CFBamIndexColBuff[ dictByPKey.values().size() ];
		Iterator< CFBamIndexColBuff > iter = dictByPKey.values().iterator();
		int idx = 0;
		while( iter.hasNext() ) {
			retList[ idx++ ] = iter.next();
		}
		return( retList );
	}

	public CFBamIndexColBuff readDerivedByUNameIdx( CFSecAuthorization Authorization,
		long TenantId,
		long IndexId,
		String Name )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByUNameIdx";
		CFBamIndexColByUNameIdxKey key = schema.getFactoryIndexCol().newUNameIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredIndexId( IndexId );
		key.setRequiredName( Name );

		CFBamIndexColBuff buff;
		if( dictByUNameIdx.containsKey( key ) ) {
			buff = dictByUNameIdx.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamIndexColBuff[] readDerivedByIdxColTenantIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByIdxColTenantIdx";
		CFBamIndexColByIdxColTenantIdxKey key = schema.getFactoryIndexCol().newIdxColTenantIdxKey();
		key.setRequiredTenantId( TenantId );

		CFBamIndexColBuff[] recArray;
		if( dictByIdxColTenantIdx.containsKey( key ) ) {
			Map< CFBamIndexColPKey, CFBamIndexColBuff > subdictIdxColTenantIdx
				= dictByIdxColTenantIdx.get( key );
			recArray = new CFBamIndexColBuff[ subdictIdxColTenantIdx.size() ];
			Iterator< CFBamIndexColBuff > iter = subdictIdxColTenantIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamIndexColPKey, CFBamIndexColBuff > subdictIdxColTenantIdx
				= new HashMap< CFBamIndexColPKey, CFBamIndexColBuff >();
			dictByIdxColTenantIdx.put( key, subdictIdxColTenantIdx );
			recArray = new CFBamIndexColBuff[0];
		}
		return( recArray );
	}

	public CFBamIndexColBuff[] readDerivedByIndexIdx( CFSecAuthorization Authorization,
		long TenantId,
		long IndexId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByIndexIdx";
		CFBamIndexColByIndexIdxKey key = schema.getFactoryIndexCol().newIndexIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredIndexId( IndexId );

		CFBamIndexColBuff[] recArray;
		if( dictByIndexIdx.containsKey( key ) ) {
			Map< CFBamIndexColPKey, CFBamIndexColBuff > subdictIndexIdx
				= dictByIndexIdx.get( key );
			recArray = new CFBamIndexColBuff[ subdictIndexIdx.size() ];
			Iterator< CFBamIndexColBuff > iter = subdictIndexIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamIndexColPKey, CFBamIndexColBuff > subdictIndexIdx
				= new HashMap< CFBamIndexColPKey, CFBamIndexColBuff >();
			dictByIndexIdx.put( key, subdictIndexIdx );
			recArray = new CFBamIndexColBuff[0];
		}
		return( recArray );
	}

	public CFBamIndexColBuff[] readDerivedByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByDefSchemaIdx";
		CFBamIndexColByDefSchemaIdxKey key = schema.getFactoryIndexCol().newDefSchemaIdxKey();
		key.setOptionalDefSchemaTenantId( DefSchemaTenantId );
		key.setOptionalDefSchemaId( DefSchemaId );

		CFBamIndexColBuff[] recArray;
		if( dictByDefSchemaIdx.containsKey( key ) ) {
			Map< CFBamIndexColPKey, CFBamIndexColBuff > subdictDefSchemaIdx
				= dictByDefSchemaIdx.get( key );
			recArray = new CFBamIndexColBuff[ subdictDefSchemaIdx.size() ];
			Iterator< CFBamIndexColBuff > iter = subdictDefSchemaIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamIndexColPKey, CFBamIndexColBuff > subdictDefSchemaIdx
				= new HashMap< CFBamIndexColPKey, CFBamIndexColBuff >();
			dictByDefSchemaIdx.put( key, subdictDefSchemaIdx );
			recArray = new CFBamIndexColBuff[0];
		}
		return( recArray );
	}

	public CFBamIndexColBuff[] readDerivedByColIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ColumnId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByColIdx";
		CFBamIndexColByColIdxKey key = schema.getFactoryIndexCol().newColIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredColumnId( ColumnId );

		CFBamIndexColBuff[] recArray;
		if( dictByColIdx.containsKey( key ) ) {
			Map< CFBamIndexColPKey, CFBamIndexColBuff > subdictColIdx
				= dictByColIdx.get( key );
			recArray = new CFBamIndexColBuff[ subdictColIdx.size() ];
			Iterator< CFBamIndexColBuff > iter = subdictColIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamIndexColPKey, CFBamIndexColBuff > subdictColIdx
				= new HashMap< CFBamIndexColPKey, CFBamIndexColBuff >();
			dictByColIdx.put( key, subdictColIdx );
			recArray = new CFBamIndexColBuff[0];
		}
		return( recArray );
	}

	public CFBamIndexColBuff[] readDerivedByPrevIdx( CFSecAuthorization Authorization,
		Long PrevTenantId,
		Long PrevId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByPrevIdx";
		CFBamIndexColByPrevIdxKey key = schema.getFactoryIndexCol().newPrevIdxKey();
		key.setOptionalPrevTenantId( PrevTenantId );
		key.setOptionalPrevId( PrevId );

		CFBamIndexColBuff[] recArray;
		if( dictByPrevIdx.containsKey( key ) ) {
			Map< CFBamIndexColPKey, CFBamIndexColBuff > subdictPrevIdx
				= dictByPrevIdx.get( key );
			recArray = new CFBamIndexColBuff[ subdictPrevIdx.size() ];
			Iterator< CFBamIndexColBuff > iter = subdictPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamIndexColPKey, CFBamIndexColBuff > subdictPrevIdx
				= new HashMap< CFBamIndexColPKey, CFBamIndexColBuff >();
			dictByPrevIdx.put( key, subdictPrevIdx );
			recArray = new CFBamIndexColBuff[0];
		}
		return( recArray );
	}

	public CFBamIndexColBuff[] readDerivedByNextIdx( CFSecAuthorization Authorization,
		Long NextTenantId,
		Long NextId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByNextIdx";
		CFBamIndexColByNextIdxKey key = schema.getFactoryIndexCol().newNextIdxKey();
		key.setOptionalNextTenantId( NextTenantId );
		key.setOptionalNextId( NextId );

		CFBamIndexColBuff[] recArray;
		if( dictByNextIdx.containsKey( key ) ) {
			Map< CFBamIndexColPKey, CFBamIndexColBuff > subdictNextIdx
				= dictByNextIdx.get( key );
			recArray = new CFBamIndexColBuff[ subdictNextIdx.size() ];
			Iterator< CFBamIndexColBuff > iter = subdictNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamIndexColPKey, CFBamIndexColBuff > subdictNextIdx
				= new HashMap< CFBamIndexColPKey, CFBamIndexColBuff >();
			dictByNextIdx.put( key, subdictNextIdx );
			recArray = new CFBamIndexColBuff[0];
		}
		return( recArray );
	}

	public CFBamIndexColBuff[] readDerivedByIdxPrevIdx( CFSecAuthorization Authorization,
		long TenantId,
		long IndexId,
		Long PrevId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByIdxPrevIdx";
		CFBamIndexColByIdxPrevIdxKey key = schema.getFactoryIndexCol().newIdxPrevIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredIndexId( IndexId );
		key.setOptionalPrevId( PrevId );

		CFBamIndexColBuff[] recArray;
		if( dictByIdxPrevIdx.containsKey( key ) ) {
			Map< CFBamIndexColPKey, CFBamIndexColBuff > subdictIdxPrevIdx
				= dictByIdxPrevIdx.get( key );
			recArray = new CFBamIndexColBuff[ subdictIdxPrevIdx.size() ];
			Iterator< CFBamIndexColBuff > iter = subdictIdxPrevIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamIndexColPKey, CFBamIndexColBuff > subdictIdxPrevIdx
				= new HashMap< CFBamIndexColPKey, CFBamIndexColBuff >();
			dictByIdxPrevIdx.put( key, subdictIdxPrevIdx );
			recArray = new CFBamIndexColBuff[0];
		}
		return( recArray );
	}

	public CFBamIndexColBuff[] readDerivedByIdxNextIdx( CFSecAuthorization Authorization,
		long TenantId,
		long IndexId,
		Long NextId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByIdxNextIdx";
		CFBamIndexColByIdxNextIdxKey key = schema.getFactoryIndexCol().newIdxNextIdxKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredIndexId( IndexId );
		key.setOptionalNextId( NextId );

		CFBamIndexColBuff[] recArray;
		if( dictByIdxNextIdx.containsKey( key ) ) {
			Map< CFBamIndexColPKey, CFBamIndexColBuff > subdictIdxNextIdx
				= dictByIdxNextIdx.get( key );
			recArray = new CFBamIndexColBuff[ subdictIdxNextIdx.size() ];
			Iterator< CFBamIndexColBuff > iter = subdictIdxNextIdx.values().iterator();
			int idx = 0;
			while( iter.hasNext() ) {
				recArray[ idx++ ] = iter.next();
			}
		}
		else {
			Map< CFBamIndexColPKey, CFBamIndexColBuff > subdictIdxNextIdx
				= new HashMap< CFBamIndexColPKey, CFBamIndexColBuff >();
			dictByIdxNextIdx.put( key, subdictIdxNextIdx );
			recArray = new CFBamIndexColBuff[0];
		}
		return( recArray );
	}

	public CFBamIndexColBuff readDerivedByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamIndexCol.readDerivedByIdIdx() ";
		CFBamIndexColPKey key = schema.getFactoryIndexCol().newPKey();
		key.setRequiredTenantId( TenantId );
		key.setRequiredId( Id );

		CFBamIndexColBuff buff;
		if( dictByPKey.containsKey( key ) ) {
			buff = dictByPKey.get( key );
		}
		else {
			buff = null;
		}
		return( buff );
	}

	public CFBamIndexColBuff readBuff( CFSecAuthorization Authorization,
		CFBamIndexColPKey PKey )
	{
		final String S_ProcName = "CFBamRamIndexCol.readBuff";
		CFBamIndexColBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a822" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamIndexColBuff lockBuff( CFSecAuthorization Authorization,
		CFBamIndexColPKey PKey )
	{
		final String S_ProcName = "lockBuff";
		CFBamIndexColBuff buff = readDerived( Authorization, PKey );
		if( ( buff != null ) && ( ! buff.getClassCode().equals( "a822" ) ) ) {
			buff = null;
		}
		return( buff );
	}

	public CFBamIndexColBuff[] readAllBuff( CFSecAuthorization Authorization )
	{
		final String S_ProcName = "CFBamRamIndexCol.readAllBuff";
		CFBamIndexColBuff buff;
		ArrayList<CFBamIndexColBuff> filteredList = new ArrayList<CFBamIndexColBuff>();
		CFBamIndexColBuff[] buffList = readAllDerived( Authorization );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a822" ) ) {
				filteredList.add( buff );
			}
		}
		return( filteredList.toArray( new CFBamIndexColBuff[0] ) );
	}

	public CFBamIndexColBuff readBuffByIdIdx( CFSecAuthorization Authorization,
		long TenantId,
		long Id )
	{
		final String S_ProcName = "CFBamRamIndexCol.readBuffByIdIdx() ";
		CFBamIndexColBuff buff = readDerivedByIdIdx( Authorization,
			TenantId,
			Id );
		if( ( buff != null ) && buff.getClassCode().equals( "a822" ) ) {
			return( (CFBamIndexColBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamIndexColBuff readBuffByUNameIdx( CFSecAuthorization Authorization,
		long TenantId,
		long IndexId,
		String Name )
	{
		final String S_ProcName = "CFBamRamIndexCol.readBuffByUNameIdx() ";
		CFBamIndexColBuff buff = readDerivedByUNameIdx( Authorization,
			TenantId,
			IndexId,
			Name );
		if( ( buff != null ) && buff.getClassCode().equals( "a822" ) ) {
			return( (CFBamIndexColBuff)buff );
		}
		else {
			return( null );
		}
	}

	public CFBamIndexColBuff[] readBuffByIdxColTenantIdx( CFSecAuthorization Authorization,
		long TenantId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readBuffByIdxColTenantIdx() ";
		CFBamIndexColBuff buff;
		ArrayList<CFBamIndexColBuff> filteredList = new ArrayList<CFBamIndexColBuff>();
		CFBamIndexColBuff[] buffList = readDerivedByIdxColTenantIdx( Authorization,
			TenantId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a822" ) ) {
				filteredList.add( (CFBamIndexColBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamIndexColBuff[0] ) );
	}

	public CFBamIndexColBuff[] readBuffByIndexIdx( CFSecAuthorization Authorization,
		long TenantId,
		long IndexId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readBuffByIndexIdx() ";
		CFBamIndexColBuff buff;
		ArrayList<CFBamIndexColBuff> filteredList = new ArrayList<CFBamIndexColBuff>();
		CFBamIndexColBuff[] buffList = readDerivedByIndexIdx( Authorization,
			TenantId,
			IndexId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a822" ) ) {
				filteredList.add( (CFBamIndexColBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamIndexColBuff[0] ) );
	}

	public CFBamIndexColBuff[] readBuffByDefSchemaIdx( CFSecAuthorization Authorization,
		Long DefSchemaTenantId,
		Long DefSchemaId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readBuffByDefSchemaIdx() ";
		CFBamIndexColBuff buff;
		ArrayList<CFBamIndexColBuff> filteredList = new ArrayList<CFBamIndexColBuff>();
		CFBamIndexColBuff[] buffList = readDerivedByDefSchemaIdx( Authorization,
			DefSchemaTenantId,
			DefSchemaId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a822" ) ) {
				filteredList.add( (CFBamIndexColBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamIndexColBuff[0] ) );
	}

	public CFBamIndexColBuff[] readBuffByColIdx( CFSecAuthorization Authorization,
		long TenantId,
		long ColumnId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readBuffByColIdx() ";
		CFBamIndexColBuff buff;
		ArrayList<CFBamIndexColBuff> filteredList = new ArrayList<CFBamIndexColBuff>();
		CFBamIndexColBuff[] buffList = readDerivedByColIdx( Authorization,
			TenantId,
			ColumnId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a822" ) ) {
				filteredList.add( (CFBamIndexColBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamIndexColBuff[0] ) );
	}

	public CFBamIndexColBuff[] readBuffByPrevIdx( CFSecAuthorization Authorization,
		Long PrevTenantId,
		Long PrevId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readBuffByPrevIdx() ";
		CFBamIndexColBuff buff;
		ArrayList<CFBamIndexColBuff> filteredList = new ArrayList<CFBamIndexColBuff>();
		CFBamIndexColBuff[] buffList = readDerivedByPrevIdx( Authorization,
			PrevTenantId,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a822" ) ) {
				filteredList.add( (CFBamIndexColBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamIndexColBuff[0] ) );
	}

	public CFBamIndexColBuff[] readBuffByNextIdx( CFSecAuthorization Authorization,
		Long NextTenantId,
		Long NextId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readBuffByNextIdx() ";
		CFBamIndexColBuff buff;
		ArrayList<CFBamIndexColBuff> filteredList = new ArrayList<CFBamIndexColBuff>();
		CFBamIndexColBuff[] buffList = readDerivedByNextIdx( Authorization,
			NextTenantId,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a822" ) ) {
				filteredList.add( (CFBamIndexColBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamIndexColBuff[0] ) );
	}

	public CFBamIndexColBuff[] readBuffByIdxPrevIdx( CFSecAuthorization Authorization,
		long TenantId,
		long IndexId,
		Long PrevId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readBuffByIdxPrevIdx() ";
		CFBamIndexColBuff buff;
		ArrayList<CFBamIndexColBuff> filteredList = new ArrayList<CFBamIndexColBuff>();
		CFBamIndexColBuff[] buffList = readDerivedByIdxPrevIdx( Authorization,
			TenantId,
			IndexId,
			PrevId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a822" ) ) {
				filteredList.add( (CFBamIndexColBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamIndexColBuff[0] ) );
	}

	public CFBamIndexColBuff[] readBuffByIdxNextIdx( CFSecAuthorization Authorization,
		long TenantId,
		long IndexId,
		Long NextId )
	{
		final String S_ProcName = "CFBamRamIndexCol.readBuffByIdxNextIdx() ";
		CFBamIndexColBuff buff;
		ArrayList<CFBamIndexColBuff> filteredList = new ArrayList<CFBamIndexColBuff>();
		CFBamIndexColBuff[] buffList = readDerivedByIdxNextIdx( Authorization,
			TenantId,
			IndexId,
			NextId );
		for( int idx = 0; idx < buffList.length; idx ++ ) {
			buff = buffList[idx];
			if( ( buff != null ) && buff.getClassCode().equals( "a822" ) ) {
				filteredList.add( (CFBamIndexColBuff)buff );
			}
		}
		return( filteredList.toArray( new CFBamIndexColBuff[0] ) );
	}

	/**
	 *	Move the specified buffer up in the chain (i.e. to the previous position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public CFBamIndexColBuff moveBuffUp( CFSecAuthorization Authorization,
		long TenantId,
		long Id,
		int revision )
	{
		final String S_ProcName = "moveBuffUp";

		CFBamIndexColBuff grandprev = null;
		CFBamIndexColBuff prev = null;
		CFBamIndexColBuff cur = null;
		CFBamIndexColBuff next = null;

		cur = schema.getTableIndexCol().readDerivedByIdIdx(Authorization, TenantId, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object" );
		}

		if( ( cur.getOptionalPrevTenantId() == null )
			&& ( cur.getOptionalPrevId() == null ) )
		{
			return( (CFBamIndexColBuff)cur );
		}

		prev = schema.getTableIndexCol().readDerivedByIdIdx(Authorization, cur.getOptionalPrevTenantId(), cur.getOptionalPrevId() );
		if( prev == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object.prev" );
		}

		if( ( prev.getOptionalPrevTenantId() != null )
			&& ( prev.getOptionalPrevId() != null ) )
		{
			grandprev = schema.getTableIndexCol().readDerivedByIdIdx(Authorization, prev.getOptionalPrevTenantId(), prev.getOptionalPrevId() );
			if( grandprev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.prev.prev" );
			}
		}

		if( ( cur.getOptionalNextTenantId() != null )
			&& ( cur.getOptionalNextId() != null ) )
		{
			next = schema.getTableIndexCol().readDerivedByIdIdx(Authorization, cur.getOptionalNextTenantId(), cur.getOptionalNextId() );
			if( next == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.next" );
			}
		}

		String classCode = prev.getClassCode();
		CFBamIndexColBuff newInstance;
			if( classCode.equals( "a822" ) ) {
				newInstance = schema.getFactoryIndexCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamIndexColBuff editPrev = newInstance;
		editPrev.set( prev );

		classCode = cur.getClassCode();
			if( classCode.equals( "a822" ) ) {
				newInstance = schema.getFactoryIndexCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamIndexColBuff editCur = newInstance;
		editCur.set( cur );

		CFBamIndexColBuff editGrandprev = null;
		if( grandprev != null ) {
			classCode = grandprev.getClassCode();
			if( classCode.equals( "a822" ) ) {
				newInstance = schema.getFactoryIndexCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editGrandprev = newInstance;
			editGrandprev.set( grandprev );
		}

		CFBamIndexColBuff editNext = null;
		if( next != null ) {
			classCode = next.getClassCode();
			if( classCode.equals( "a822" ) ) {
				newInstance = schema.getFactoryIndexCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editNext = newInstance;
			editNext.set( next );
		}

		if( editGrandprev != null ) {
			editGrandprev.setOptionalNextTenantId( cur.getRequiredTenantId() );
			editGrandprev.setOptionalNextId( cur.getRequiredId() );
			editCur.setOptionalPrevTenantId( grandprev.getRequiredTenantId() );
			editCur.setOptionalPrevId( grandprev.getRequiredId() );
		}
		else {
			editCur.setOptionalPrevTenantId( null );
			editCur.setOptionalPrevId( null );
		}

			editPrev.setOptionalPrevTenantId( cur.getRequiredTenantId() );
			editPrev.setOptionalPrevId( cur.getRequiredId() );

			editCur.setOptionalNextTenantId( prev.getRequiredTenantId() );
			editCur.setOptionalNextId( prev.getRequiredId() );

		if( next != null ) {
			editPrev.setOptionalNextTenantId( next.getRequiredTenantId() );
			editPrev.setOptionalNextId( next.getRequiredId() );
			editNext.setOptionalPrevTenantId( prev.getRequiredTenantId() );
			editNext.setOptionalPrevId( prev.getRequiredId() );
		}
		else {
			editPrev.setOptionalNextTenantId( null );
			editPrev.setOptionalNextId( null );
		}

		if( editGrandprev != null ) {
			classCode = editGrandprev.getClassCode();
			if( classCode.equals( "a822" ) ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editGrandprev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		classCode = editPrev.getClassCode();
			if( classCode.equals( "a822" ) ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		classCode = editCur.getClassCode();
			if( classCode.equals( "a822" ) ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		if( editNext != null ) {
			classCode = editNext.getClassCode();
			if( classCode.equals( "a822" ) ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		return( (CFBamIndexColBuff)editCur );
	}

	/**
	 *	Move the specified buffer down in the chain (i.e. to the next position.)
	 *
	 *	@return	The refreshed buffer after it has been moved
	 */
	public CFBamIndexColBuff moveBuffDown( CFSecAuthorization Authorization,
		long TenantId,
		long Id,
		int revision )
	{
		final String S_ProcName = "moveBuffDown";

		CFBamIndexColBuff prev = null;
		CFBamIndexColBuff cur = null;
		CFBamIndexColBuff next = null;
		CFBamIndexColBuff grandnext = null;

		cur = schema.getTableIndexCol().readDerivedByIdIdx(Authorization, TenantId, Id);
		if( cur == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object" );
		}

		if( ( cur.getOptionalNextTenantId() == null )
			&& ( cur.getOptionalNextId() == null ) )
		{
			return( (CFBamIndexColBuff)cur );
		}

		next = schema.getTableIndexCol().readDerivedByIdIdx(Authorization, cur.getOptionalNextTenantId(), cur.getOptionalNextId() );
		if( next == null ) {
			throw new CFLibCollisionDetectedException( getClass(),
				S_ProcName,
				"Could not locate object.next" );
		}

		if( ( next.getOptionalNextTenantId() != null )
			&& ( next.getOptionalNextId() != null ) )
		{
			grandnext = schema.getTableIndexCol().readDerivedByIdIdx(Authorization, next.getOptionalNextTenantId(), next.getOptionalNextId() );
			if( grandnext == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.next.next" );
			}
		}

		if( ( cur.getOptionalPrevTenantId() != null )
			&& ( cur.getOptionalPrevId() != null ) )
		{
			prev = schema.getTableIndexCol().readDerivedByIdIdx(Authorization, cur.getOptionalPrevTenantId(), cur.getOptionalPrevId() );
			if( prev == null ) {
				throw new CFLibCollisionDetectedException( getClass(),
					S_ProcName,
					"Could not locate object.prev" );
			}
		}

		String classCode = cur.getClassCode();
		CFBamIndexColBuff newInstance;
			if( classCode.equals( "a822" ) ) {
				newInstance = schema.getFactoryIndexCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamIndexColBuff editCur = newInstance;
		editCur.set( cur );

		classCode = next.getClassCode();
			if( classCode.equals( "a822" ) ) {
				newInstance = schema.getFactoryIndexCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		CFBamIndexColBuff editNext = newInstance;
		editNext.set( next );

		CFBamIndexColBuff editGrandnext = null;
		if( grandnext != null ) {
			classCode = grandnext.getClassCode();
			if( classCode.equals( "a822" ) ) {
				newInstance = schema.getFactoryIndexCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editGrandnext = newInstance;
			editGrandnext.set( grandnext );
		}

		CFBamIndexColBuff editPrev = null;
		if( prev != null ) {
			classCode = prev.getClassCode();
			if( classCode.equals( "a822" ) ) {
				newInstance = schema.getFactoryIndexCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editPrev = newInstance;
			editPrev.set( prev );
		}

		if( prev != null ) {
			editPrev.setOptionalNextTenantId( next.getRequiredTenantId() );
			editPrev.setOptionalNextId( next.getRequiredId() );
			editNext.setOptionalPrevTenantId( prev.getRequiredTenantId() );
			editNext.setOptionalPrevId( prev.getRequiredId() );
		}
		else {
			editNext.setOptionalPrevTenantId( null );
			editNext.setOptionalPrevId( null );
		}

			editCur.setOptionalPrevTenantId( next.getRequiredTenantId() );
			editCur.setOptionalPrevId( next.getRequiredId() );

			editNext.setOptionalNextTenantId( cur.getRequiredTenantId() );
			editNext.setOptionalNextId( cur.getRequiredId() );

		if( editGrandnext != null ) {
			editCur.setOptionalNextTenantId( grandnext.getRequiredTenantId() );
			editCur.setOptionalNextId( grandnext.getRequiredId() );
			editGrandnext.setOptionalPrevTenantId( cur.getRequiredTenantId() );
			editGrandnext.setOptionalPrevId( cur.getRequiredId() );
		}
		else {
			editCur.setOptionalNextTenantId( null );
			editCur.setOptionalNextId( null );
		}

		if( editPrev != null ) {
			classCode = editPrev.getClassCode();
			if( classCode.equals( "a822" ) ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		classCode = editCur.getClassCode();
			if( classCode.equals( "a822" ) ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editCur );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		classCode = editNext.getClassCode();
			if( classCode.equals( "a822" ) ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}

		if( editGrandnext != null ) {
			classCode = editGrandnext.getClassCode();
			if( classCode.equals( "a822" ) ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editGrandnext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		return( (CFBamIndexColBuff)editCur );
	}

	public void updateIndexCol( CFSecAuthorization Authorization,
		CFBamIndexColBuff Buff )
	{
		CFBamIndexColPKey pkey = schema.getFactoryIndexCol().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamIndexColBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			throw new CFLibStaleCacheDetectedException( getClass(),
				"updateIndexCol",
				"Existing record not found",
				"IndexCol",
				pkey );
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() ) {
			throw new CFLibCollisionDetectedException( getClass(),
				"updateIndexCol",
				pkey );
		}
		Buff.setRequiredRevision( Buff.getRequiredRevision() + 1 );
		CFBamIndexColByUNameIdxKey existingKeyUNameIdx = schema.getFactoryIndexCol().newUNameIdxKey();
		existingKeyUNameIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeyUNameIdx.setRequiredIndexId( existing.getRequiredIndexId() );
		existingKeyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamIndexColByUNameIdxKey newKeyUNameIdx = schema.getFactoryIndexCol().newUNameIdxKey();
		newKeyUNameIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeyUNameIdx.setRequiredIndexId( Buff.getRequiredIndexId() );
		newKeyUNameIdx.setRequiredName( Buff.getRequiredName() );

		CFBamIndexColByIdxColTenantIdxKey existingKeyIdxColTenantIdx = schema.getFactoryIndexCol().newIdxColTenantIdxKey();
		existingKeyIdxColTenantIdx.setRequiredTenantId( existing.getRequiredTenantId() );

		CFBamIndexColByIdxColTenantIdxKey newKeyIdxColTenantIdx = schema.getFactoryIndexCol().newIdxColTenantIdxKey();
		newKeyIdxColTenantIdx.setRequiredTenantId( Buff.getRequiredTenantId() );

		CFBamIndexColByIndexIdxKey existingKeyIndexIdx = schema.getFactoryIndexCol().newIndexIdxKey();
		existingKeyIndexIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeyIndexIdx.setRequiredIndexId( existing.getRequiredIndexId() );

		CFBamIndexColByIndexIdxKey newKeyIndexIdx = schema.getFactoryIndexCol().newIndexIdxKey();
		newKeyIndexIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeyIndexIdx.setRequiredIndexId( Buff.getRequiredIndexId() );

		CFBamIndexColByDefSchemaIdxKey existingKeyDefSchemaIdx = schema.getFactoryIndexCol().newDefSchemaIdxKey();
		existingKeyDefSchemaIdx.setOptionalDefSchemaTenantId( existing.getOptionalDefSchemaTenantId() );
		existingKeyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamIndexColByDefSchemaIdxKey newKeyDefSchemaIdx = schema.getFactoryIndexCol().newDefSchemaIdxKey();
		newKeyDefSchemaIdx.setOptionalDefSchemaTenantId( Buff.getOptionalDefSchemaTenantId() );
		newKeyDefSchemaIdx.setOptionalDefSchemaId( Buff.getOptionalDefSchemaId() );

		CFBamIndexColByColIdxKey existingKeyColIdx = schema.getFactoryIndexCol().newColIdxKey();
		existingKeyColIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeyColIdx.setRequiredColumnId( existing.getRequiredColumnId() );

		CFBamIndexColByColIdxKey newKeyColIdx = schema.getFactoryIndexCol().newColIdxKey();
		newKeyColIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeyColIdx.setRequiredColumnId( Buff.getRequiredColumnId() );

		CFBamIndexColByPrevIdxKey existingKeyPrevIdx = schema.getFactoryIndexCol().newPrevIdxKey();
		existingKeyPrevIdx.setOptionalPrevTenantId( existing.getOptionalPrevTenantId() );
		existingKeyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamIndexColByPrevIdxKey newKeyPrevIdx = schema.getFactoryIndexCol().newPrevIdxKey();
		newKeyPrevIdx.setOptionalPrevTenantId( Buff.getOptionalPrevTenantId() );
		newKeyPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamIndexColByNextIdxKey existingKeyNextIdx = schema.getFactoryIndexCol().newNextIdxKey();
		existingKeyNextIdx.setOptionalNextTenantId( existing.getOptionalNextTenantId() );
		existingKeyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamIndexColByNextIdxKey newKeyNextIdx = schema.getFactoryIndexCol().newNextIdxKey();
		newKeyNextIdx.setOptionalNextTenantId( Buff.getOptionalNextTenantId() );
		newKeyNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		CFBamIndexColByIdxPrevIdxKey existingKeyIdxPrevIdx = schema.getFactoryIndexCol().newIdxPrevIdxKey();
		existingKeyIdxPrevIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeyIdxPrevIdx.setRequiredIndexId( existing.getRequiredIndexId() );
		existingKeyIdxPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamIndexColByIdxPrevIdxKey newKeyIdxPrevIdx = schema.getFactoryIndexCol().newIdxPrevIdxKey();
		newKeyIdxPrevIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeyIdxPrevIdx.setRequiredIndexId( Buff.getRequiredIndexId() );
		newKeyIdxPrevIdx.setOptionalPrevId( Buff.getOptionalPrevId() );

		CFBamIndexColByIdxNextIdxKey existingKeyIdxNextIdx = schema.getFactoryIndexCol().newIdxNextIdxKey();
		existingKeyIdxNextIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		existingKeyIdxNextIdx.setRequiredIndexId( existing.getRequiredIndexId() );
		existingKeyIdxNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamIndexColByIdxNextIdxKey newKeyIdxNextIdx = schema.getFactoryIndexCol().newIdxNextIdxKey();
		newKeyIdxNextIdx.setRequiredTenantId( Buff.getRequiredTenantId() );
		newKeyIdxNextIdx.setRequiredIndexId( Buff.getRequiredIndexId() );
		newKeyIdxNextIdx.setOptionalNextId( Buff.getOptionalNextId() );

		// Check unique indexes

		if( ! existingKeyUNameIdx.equals( newKeyUNameIdx ) ) {
			if( dictByUNameIdx.containsKey( newKeyUNameIdx ) ) {
				throw new CFLibUniqueIndexViolationException( getClass(),
					"updateIndexCol",
					"IndexColUNameIdx",
					newKeyUNameIdx );
			}
		}

		// Validate foreign keys

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableTenant().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateIndexCol",
						"Owner",
						"Tenant",
						"Tenant",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableIndex().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredIndexId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateIndexCol",
						"Container",
						"Index",
						"Index",
						null );
				}
			}
		}

		{
			boolean allNull = true;

			if( allNull ) {
				if( null == schema.getTableValue().readDerivedByIdIdx( Authorization,
						Buff.getRequiredTenantId(),
						Buff.getRequiredColumnId() ) )
				{
					throw new CFLibUnresolvedRelationException( getClass(),
						"updateIndexCol",
						"Lookup",
						"Column",
						"Value",
						null );
				}
			}
		}

		// Update is valid

		Map< CFBamIndexColPKey, CFBamIndexColBuff > subdict;

		dictByPKey.remove( pkey );
		dictByPKey.put( pkey, Buff );

		dictByUNameIdx.remove( existingKeyUNameIdx );
		dictByUNameIdx.put( newKeyUNameIdx, Buff );

		subdict = dictByIdxColTenantIdx.get( existingKeyIdxColTenantIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByIdxColTenantIdx.containsKey( newKeyIdxColTenantIdx ) ) {
			subdict = dictByIdxColTenantIdx.get( newKeyIdxColTenantIdx );
		}
		else {
			subdict = new HashMap< CFBamIndexColPKey, CFBamIndexColBuff >();
			dictByIdxColTenantIdx.put( newKeyIdxColTenantIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByIndexIdx.get( existingKeyIndexIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByIndexIdx.containsKey( newKeyIndexIdx ) ) {
			subdict = dictByIndexIdx.get( newKeyIndexIdx );
		}
		else {
			subdict = new HashMap< CFBamIndexColPKey, CFBamIndexColBuff >();
			dictByIndexIdx.put( newKeyIndexIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByDefSchemaIdx.get( existingKeyDefSchemaIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByDefSchemaIdx.containsKey( newKeyDefSchemaIdx ) ) {
			subdict = dictByDefSchemaIdx.get( newKeyDefSchemaIdx );
		}
		else {
			subdict = new HashMap< CFBamIndexColPKey, CFBamIndexColBuff >();
			dictByDefSchemaIdx.put( newKeyDefSchemaIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByColIdx.get( existingKeyColIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByColIdx.containsKey( newKeyColIdx ) ) {
			subdict = dictByColIdx.get( newKeyColIdx );
		}
		else {
			subdict = new HashMap< CFBamIndexColPKey, CFBamIndexColBuff >();
			dictByColIdx.put( newKeyColIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByPrevIdx.get( existingKeyPrevIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByPrevIdx.containsKey( newKeyPrevIdx ) ) {
			subdict = dictByPrevIdx.get( newKeyPrevIdx );
		}
		else {
			subdict = new HashMap< CFBamIndexColPKey, CFBamIndexColBuff >();
			dictByPrevIdx.put( newKeyPrevIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByNextIdx.get( existingKeyNextIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByNextIdx.containsKey( newKeyNextIdx ) ) {
			subdict = dictByNextIdx.get( newKeyNextIdx );
		}
		else {
			subdict = new HashMap< CFBamIndexColPKey, CFBamIndexColBuff >();
			dictByNextIdx.put( newKeyNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByIdxPrevIdx.get( existingKeyIdxPrevIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByIdxPrevIdx.containsKey( newKeyIdxPrevIdx ) ) {
			subdict = dictByIdxPrevIdx.get( newKeyIdxPrevIdx );
		}
		else {
			subdict = new HashMap< CFBamIndexColPKey, CFBamIndexColBuff >();
			dictByIdxPrevIdx.put( newKeyIdxPrevIdx, subdict );
		}
		subdict.put( pkey, Buff );

		subdict = dictByIdxNextIdx.get( existingKeyIdxNextIdx );
		if( subdict != null ) {
			subdict.remove( pkey );
		}
		if( dictByIdxNextIdx.containsKey( newKeyIdxNextIdx ) ) {
			subdict = dictByIdxNextIdx.get( newKeyIdxNextIdx );
		}
		else {
			subdict = new HashMap< CFBamIndexColPKey, CFBamIndexColBuff >();
			dictByIdxNextIdx.put( newKeyIdxNextIdx, subdict );
		}
		subdict.put( pkey, Buff );

	}

	public void deleteIndexCol( CFSecAuthorization Authorization,
		CFBamIndexColBuff Buff )
	{
		final String S_ProcName = "CFBamRamIndexColTable.deleteIndexCol() ";
		String classCode;
		CFBamIndexColPKey pkey = schema.getFactoryIndexCol().newPKey();
		pkey.setRequiredTenantId( Buff.getRequiredTenantId() );
		pkey.setRequiredId( Buff.getRequiredId() );
		CFBamIndexColBuff existing = dictByPKey.get( pkey );
		if( existing == null ) {
			return;
		}
		if( existing.getRequiredRevision() != Buff.getRequiredRevision() )
		{
			throw new CFLibCollisionDetectedException( getClass(),
				"deleteIndexCol",
				pkey );
		}
		long varTenantId = existing.getRequiredTenantId();
		long varIndexId = existing.getRequiredIndexId();
		CFBamIndexBuff container = schema.getTableIndex().readDerivedByIdIdx( Authorization,
			varTenantId,
			varIndexId );
		if( container == null ) {
			throw new CFLibNullArgumentException( getClass(),
				S_ProcName,
				0,
				"container" );
		}

		Long prevTenantId = existing.getOptionalPrevTenantId();
		Long prevId = existing.getOptionalPrevId();
		Long nextTenantId = existing.getOptionalNextTenantId();
		Long nextId = existing.getOptionalNextId();

		CFBamIndexColBuff prev = null;
		if( ( prevTenantId != null )
			&& ( prevId != null ) )
		{
			prev = schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				prevTenantId,
				prevId );
			if( prev == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"prev" );
			}
			CFBamIndexColBuff editPrev;
			classCode = prev.getClassCode();
			if( classCode.equals( "a822" ) ) {
				editPrev = schema.getFactoryIndexCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editPrev.set( prev );
			editPrev.setOptionalNextTenantId( nextTenantId );
			editPrev.setOptionalNextId( nextId );
			if( classCode.equals( "a822" ) ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editPrev );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

		CFBamIndexColBuff next = null;
		if( ( nextTenantId != null )
			&& ( nextId != null ) )
		{
			next = schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				nextTenantId,
				nextId );
			if( next == null ) {
				throw new CFLibNullArgumentException( getClass(),
					S_ProcName,
					0,
					"next" );
			}
			CFBamIndexColBuff editNext;
			classCode = next.getClassCode();
			if( classCode.equals( "a822" ) ) {
				editNext = schema.getFactoryIndexCol().newBuff();
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
			editNext.set( next );
			editNext.setOptionalPrevTenantId( prevTenantId );
			editNext.setOptionalPrevId( prevId );
			if( classCode.equals( "a822" ) ) {
				schema.getTableIndexCol().updateIndexCol( Authorization, editNext );
			}
			else {
				throw new CFLibUnsupportedClassException( getClass(),
					S_ProcName,
					"Unrecognized ClassCode \"" + classCode + "\"" );
			}
		}

					schema.getTableRelationCol().deleteRelationColByFromColIdx( Authorization,
						existing.getRequiredTenantId(),
						existing.getRequiredId() );
					schema.getTableRelationCol().deleteRelationColByToColIdx( Authorization,
						existing.getRequiredTenantId(),
						existing.getRequiredId() );
		CFBamIndexColByUNameIdxKey keyUNameIdx = schema.getFactoryIndexCol().newUNameIdxKey();
		keyUNameIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keyUNameIdx.setRequiredIndexId( existing.getRequiredIndexId() );
		keyUNameIdx.setRequiredName( existing.getRequiredName() );

		CFBamIndexColByIdxColTenantIdxKey keyIdxColTenantIdx = schema.getFactoryIndexCol().newIdxColTenantIdxKey();
		keyIdxColTenantIdx.setRequiredTenantId( existing.getRequiredTenantId() );

		CFBamIndexColByIndexIdxKey keyIndexIdx = schema.getFactoryIndexCol().newIndexIdxKey();
		keyIndexIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keyIndexIdx.setRequiredIndexId( existing.getRequiredIndexId() );

		CFBamIndexColByDefSchemaIdxKey keyDefSchemaIdx = schema.getFactoryIndexCol().newDefSchemaIdxKey();
		keyDefSchemaIdx.setOptionalDefSchemaTenantId( existing.getOptionalDefSchemaTenantId() );
		keyDefSchemaIdx.setOptionalDefSchemaId( existing.getOptionalDefSchemaId() );

		CFBamIndexColByColIdxKey keyColIdx = schema.getFactoryIndexCol().newColIdxKey();
		keyColIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keyColIdx.setRequiredColumnId( existing.getRequiredColumnId() );

		CFBamIndexColByPrevIdxKey keyPrevIdx = schema.getFactoryIndexCol().newPrevIdxKey();
		keyPrevIdx.setOptionalPrevTenantId( existing.getOptionalPrevTenantId() );
		keyPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamIndexColByNextIdxKey keyNextIdx = schema.getFactoryIndexCol().newNextIdxKey();
		keyNextIdx.setOptionalNextTenantId( existing.getOptionalNextTenantId() );
		keyNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		CFBamIndexColByIdxPrevIdxKey keyIdxPrevIdx = schema.getFactoryIndexCol().newIdxPrevIdxKey();
		keyIdxPrevIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keyIdxPrevIdx.setRequiredIndexId( existing.getRequiredIndexId() );
		keyIdxPrevIdx.setOptionalPrevId( existing.getOptionalPrevId() );

		CFBamIndexColByIdxNextIdxKey keyIdxNextIdx = schema.getFactoryIndexCol().newIdxNextIdxKey();
		keyIdxNextIdx.setRequiredTenantId( existing.getRequiredTenantId() );
		keyIdxNextIdx.setRequiredIndexId( existing.getRequiredIndexId() );
		keyIdxNextIdx.setOptionalNextId( existing.getOptionalNextId() );

		// Validate reverse foreign keys

		if( schema.getTableRelationCol().readDerivedByFromColIdx( Authorization,
					existing.getRequiredTenantId(),
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteIndexCol",
				"Lookup",
				"LookupFromCol",
				"RelationCol",
				pkey );
		}

		if( schema.getTableRelationCol().readDerivedByToColIdx( Authorization,
					existing.getRequiredTenantId(),
					existing.getRequiredId() ).length > 0 )
		{
			throw new CFLibDependentsDetectedException( getClass(),
				"deleteIndexCol",
				"Lookup",
				"LookupToCol",
				"RelationCol",
				pkey );
		}

		// Delete is valid
		Map< CFBamIndexColPKey, CFBamIndexColBuff > subdict;

		dictByPKey.remove( pkey );

		dictByUNameIdx.remove( keyUNameIdx );

		subdict = dictByIdxColTenantIdx.get( keyIdxColTenantIdx );
		subdict.remove( pkey );

		subdict = dictByIndexIdx.get( keyIndexIdx );
		subdict.remove( pkey );

		subdict = dictByDefSchemaIdx.get( keyDefSchemaIdx );
		subdict.remove( pkey );

		subdict = dictByColIdx.get( keyColIdx );
		subdict.remove( pkey );

		subdict = dictByPrevIdx.get( keyPrevIdx );
		subdict.remove( pkey );

		subdict = dictByNextIdx.get( keyNextIdx );
		subdict.remove( pkey );

		subdict = dictByIdxPrevIdx.get( keyIdxPrevIdx );
		subdict.remove( pkey );

		subdict = dictByIdxNextIdx.get( keyIdxNextIdx );
		subdict.remove( pkey );

	}
	public void deleteIndexColByIdIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argId )
	{
		CFBamIndexColPKey key = schema.getFactoryIndexCol().newPKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredId( argId );
		deleteIndexColByIdIdx( Authorization, key );
	}

	public void deleteIndexColByIdIdx( CFSecAuthorization Authorization,
		CFBamIndexColPKey argKey )
	{
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		CFBamIndexColBuff cur;
		LinkedList<CFBamIndexColBuff> matchSet = new LinkedList<CFBamIndexColBuff>();
		Iterator<CFBamIndexColBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexColBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteIndexCol( Authorization, cur );
		}
	}

	public void deleteIndexColByUNameIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argIndexId,
		String argName )
	{
		CFBamIndexColByUNameIdxKey key = schema.getFactoryIndexCol().newUNameIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredIndexId( argIndexId );
		key.setRequiredName( argName );
		deleteIndexColByUNameIdx( Authorization, key );
	}

	public void deleteIndexColByUNameIdx( CFSecAuthorization Authorization,
		CFBamIndexColByUNameIdxKey argKey )
	{
		CFBamIndexColBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamIndexColBuff> matchSet = new LinkedList<CFBamIndexColBuff>();
		Iterator<CFBamIndexColBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexColBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteIndexCol( Authorization, cur );
		}
	}

	public void deleteIndexColByIdxColTenantIdx( CFSecAuthorization Authorization,
		long argTenantId )
	{
		CFBamIndexColByIdxColTenantIdxKey key = schema.getFactoryIndexCol().newIdxColTenantIdxKey();
		key.setRequiredTenantId( argTenantId );
		deleteIndexColByIdxColTenantIdx( Authorization, key );
	}

	public void deleteIndexColByIdxColTenantIdx( CFSecAuthorization Authorization,
		CFBamIndexColByIdxColTenantIdxKey argKey )
	{
		CFBamIndexColBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamIndexColBuff> matchSet = new LinkedList<CFBamIndexColBuff>();
		Iterator<CFBamIndexColBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexColBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteIndexCol( Authorization, cur );
		}
	}

	public void deleteIndexColByIndexIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argIndexId )
	{
		CFBamIndexColByIndexIdxKey key = schema.getFactoryIndexCol().newIndexIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredIndexId( argIndexId );
		deleteIndexColByIndexIdx( Authorization, key );
	}

	public void deleteIndexColByIndexIdx( CFSecAuthorization Authorization,
		CFBamIndexColByIndexIdxKey argKey )
	{
		CFBamIndexColBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamIndexColBuff> matchSet = new LinkedList<CFBamIndexColBuff>();
		Iterator<CFBamIndexColBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexColBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteIndexCol( Authorization, cur );
		}
	}

	public void deleteIndexColByDefSchemaIdx( CFSecAuthorization Authorization,
		Long argDefSchemaTenantId,
		Long argDefSchemaId )
	{
		CFBamIndexColByDefSchemaIdxKey key = schema.getFactoryIndexCol().newDefSchemaIdxKey();
		key.setOptionalDefSchemaTenantId( argDefSchemaTenantId );
		key.setOptionalDefSchemaId( argDefSchemaId );
		deleteIndexColByDefSchemaIdx( Authorization, key );
	}

	public void deleteIndexColByDefSchemaIdx( CFSecAuthorization Authorization,
		CFBamIndexColByDefSchemaIdxKey argKey )
	{
		CFBamIndexColBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalDefSchemaTenantId() != null ) {
			anyNotNull = true;
		}
		if( argKey.getOptionalDefSchemaId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamIndexColBuff> matchSet = new LinkedList<CFBamIndexColBuff>();
		Iterator<CFBamIndexColBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexColBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteIndexCol( Authorization, cur );
		}
	}

	public void deleteIndexColByColIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argColumnId )
	{
		CFBamIndexColByColIdxKey key = schema.getFactoryIndexCol().newColIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredColumnId( argColumnId );
		deleteIndexColByColIdx( Authorization, key );
	}

	public void deleteIndexColByColIdx( CFSecAuthorization Authorization,
		CFBamIndexColByColIdxKey argKey )
	{
		CFBamIndexColBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamIndexColBuff> matchSet = new LinkedList<CFBamIndexColBuff>();
		Iterator<CFBamIndexColBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexColBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteIndexCol( Authorization, cur );
		}
	}

	public void deleteIndexColByPrevIdx( CFSecAuthorization Authorization,
		Long argPrevTenantId,
		Long argPrevId )
	{
		CFBamIndexColByPrevIdxKey key = schema.getFactoryIndexCol().newPrevIdxKey();
		key.setOptionalPrevTenantId( argPrevTenantId );
		key.setOptionalPrevId( argPrevId );
		deleteIndexColByPrevIdx( Authorization, key );
	}

	public void deleteIndexColByPrevIdx( CFSecAuthorization Authorization,
		CFBamIndexColByPrevIdxKey argKey )
	{
		CFBamIndexColBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalPrevTenantId() != null ) {
			anyNotNull = true;
		}
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamIndexColBuff> matchSet = new LinkedList<CFBamIndexColBuff>();
		Iterator<CFBamIndexColBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexColBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteIndexCol( Authorization, cur );
		}
	}

	public void deleteIndexColByNextIdx( CFSecAuthorization Authorization,
		Long argNextTenantId,
		Long argNextId )
	{
		CFBamIndexColByNextIdxKey key = schema.getFactoryIndexCol().newNextIdxKey();
		key.setOptionalNextTenantId( argNextTenantId );
		key.setOptionalNextId( argNextId );
		deleteIndexColByNextIdx( Authorization, key );
	}

	public void deleteIndexColByNextIdx( CFSecAuthorization Authorization,
		CFBamIndexColByNextIdxKey argKey )
	{
		CFBamIndexColBuff cur;
		boolean anyNotNull = false;
		if( argKey.getOptionalNextTenantId() != null ) {
			anyNotNull = true;
		}
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamIndexColBuff> matchSet = new LinkedList<CFBamIndexColBuff>();
		Iterator<CFBamIndexColBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexColBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteIndexCol( Authorization, cur );
		}
	}

	public void deleteIndexColByIdxPrevIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argIndexId,
		Long argPrevId )
	{
		CFBamIndexColByIdxPrevIdxKey key = schema.getFactoryIndexCol().newIdxPrevIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredIndexId( argIndexId );
		key.setOptionalPrevId( argPrevId );
		deleteIndexColByIdxPrevIdx( Authorization, key );
	}

	public void deleteIndexColByIdxPrevIdx( CFSecAuthorization Authorization,
		CFBamIndexColByIdxPrevIdxKey argKey )
	{
		CFBamIndexColBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( argKey.getOptionalPrevId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamIndexColBuff> matchSet = new LinkedList<CFBamIndexColBuff>();
		Iterator<CFBamIndexColBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexColBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteIndexCol( Authorization, cur );
		}
	}

	public void deleteIndexColByIdxNextIdx( CFSecAuthorization Authorization,
		long argTenantId,
		long argIndexId,
		Long argNextId )
	{
		CFBamIndexColByIdxNextIdxKey key = schema.getFactoryIndexCol().newIdxNextIdxKey();
		key.setRequiredTenantId( argTenantId );
		key.setRequiredIndexId( argIndexId );
		key.setOptionalNextId( argNextId );
		deleteIndexColByIdxNextIdx( Authorization, key );
	}

	public void deleteIndexColByIdxNextIdx( CFSecAuthorization Authorization,
		CFBamIndexColByIdxNextIdxKey argKey )
	{
		CFBamIndexColBuff cur;
		boolean anyNotNull = false;
		anyNotNull = true;
		anyNotNull = true;
		if( argKey.getOptionalNextId() != null ) {
			anyNotNull = true;
		}
		if( ! anyNotNull ) {
			return;
		}
		LinkedList<CFBamIndexColBuff> matchSet = new LinkedList<CFBamIndexColBuff>();
		Iterator<CFBamIndexColBuff> values = dictByPKey.values().iterator();
		while( values.hasNext() ) {
			cur = values.next();
			if( argKey.equals( cur ) ) {
				matchSet.add( cur );
			}
		}
		Iterator<CFBamIndexColBuff> iterMatch = matchSet.iterator();
		while( iterMatch.hasNext() ) {
			cur = iterMatch.next();
			cur = schema.getTableIndexCol().readDerivedByIdIdx( Authorization,
				cur.getRequiredTenantId(),
				cur.getRequiredId() );
			deleteIndexCol( Authorization, cur );
		}
	}

	public void releasePreparedStatements() {
	}
}

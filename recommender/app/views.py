from django.http import JsonResponse, HttpResponse
from django.views.decorators.csrf import csrf_exempt
import json
import os
import requests
import spotipy
from spotipy.oauth2 import SpotifyClientCredentials


os.environ['SPOTIPY_CLIENT_ID']='7a03c05c75c04958926b5213cda242f3'
os.environ['SPOTIPY_CLIENT_SECRET']='6d50816451cf45f4a7b09a0188f30fab'





def getsongs(request):
    if request.method != 'GET':
        return HttpResponse(status=404)
    auth_manager = SpotifyClientCredentials()
    sp = spotipy.Spotify(auth_manager=auth_manager)  
    track_id = '6rqhFgbbKwnb9MLmUQDhG6'  # Dummy Track
    response = {}
    response['songs'] = sp.audio_features([track_id])
    #  response['songs'] = ['Replace Me', 'DUMMY RESPONSE'] 
    return JsonResponse(response)


@csrf_exempt
def postsong(request):
    if request.method != 'POST':
        return HttpResponse(status=404)
    json_data = json.loads(request.body)
    track_id = json_data['track_id']
    auth_manager = SpotifyClientCredentials()
    sp = spotipy.Spotify(auth_manager=auth_manager)
    response = {}
    response['songs'] = sp.audio_features([track_id])
    # track_id = '6rqhFgbbKwnb9MLmUQDhG6'  # Dummy Track
    return JsonResponse(response)
  

    # json_data = json.loads(request.body)
    # username = json_data['username']
    # message = json_data['message']
    # cursor = connection.cursor()
    # cursor.execute('INSERT INTO chatts (username, message) VALUES '
    #                '(%s, %s);', (username, message))
   
